package com.project1.toystoreapp.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.API_end_points.SanPhamEndpoint;
import com.project1.toystoreapp.API_end_points.ThuongHieuEndpoint;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.ImagePicker;
import com.project1.toystoreapp.Classes.RequestPermission;
import com.project1.toystoreapp.databinding.ActivityAdminScreenBinding;
import com.project1.toystoreapp.model.ThuongHieu;
import com.project1.toystoreapp.R;

import com.project1.toystoreapp.layout.AdminQuanLyThuongHieu_fg;
import com.project1.toystoreapp.layout.Admin_QuanLy_Loai_SPCon_fg;
import com.project1.toystoreapp.layout.Admin_QuanLy_SP_fg;
import com.project1.toystoreapp.layout.Admin_QuanLy_all_fg;
import com.project1.toystoreapp.layout.Admin_Quanly_Loai_SP_fg;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.SanPham;
import com.project1.toystoreapp.model.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Admin_screen extends AppCompatActivity {
    ImagePicker imagePicker;
    private ActivityAdminScreenBinding binding;
    User account = new User();
    private String accountKey="account";
    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomAppBar.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = new ImagePicker(this);
        binding = ActivityAdminScreenBinding.inflate(getLayoutInflater());
        Intent getaccount = getIntent();
        account =(User) getaccount.getSerializableExtra(accountKey);
        if(account==null) return;

        setContentView(binding.getRoot());
        View headerdraw = binding.navigation.getHeaderView(0);
        TextView username = headerdraw.findViewById(R.id.name);
        if(username!=null){
            String[] name= account.getUsername().split(" ");
            username.setText(name[name.length-1]);
        }

        Fragment fragment = new Admin_QuanLy_all_fg();
        setFragment(fragment,false);
        getSupportFragmentManager().addOnBackStackChangedListener(this::settupCurrentFG);
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fg =getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                if(item.getItemId()==R.id.home){
                    getOnBackPressedDispatcher().onBackPressed();
                }else if (item.getItemId()==R.id.search){
                    if(fg instanceof Admin_Quanly_Loai_SP_fg){
                        Toast.makeText(Admin_screen.this, "searrchq", Toast.LENGTH_SHORT).show();
                    } else if (fg instanceof Admin_QuanLy_SP_fg) {
                        ((Admin_QuanLy_SP_fg)fg).search(true);
                    }
                }
                return true;
            }
        });
        binding.navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.logout){
                    SharedPreferences sharedPreferences = getSharedPreferences(accountKey, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(Admin_screen.this,Login.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }

    public void setFragment(Fragment fg,boolean addtostack){
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout,fg);
            if(addtostack) {
                transaction.addToBackStack(fg.getClass().getName());
                Log.e("DebugBackStack", "Stack: "+fg.getClass().getName());
            }
            transaction.commit();
            getSupportFragmentManager().executePendingTransactions();
            settupCurrentFG();
    }
//    public boolean isSameStack(String tag){
//        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
//        if(backStackCount>0){
//            FragmentManager.BackStackEntry lastEntry = getSupportFragmentManager().getBackStackEntryAt(backStackCount - 1);
//            if(tag.equals(lastEntry.getName())) return true;
//        }
//        return false;
//    }
    public void settupCurrentFG(){
        binding.bottomAppBar.setVisibility(View.VISIBLE);
        binding.add.show();
        binding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        Fragment curr = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.baseline_add_24);
        binding.add.setImageDrawable(drawable);
        if(curr instanceof Admin_QuanLy_all_fg){
            setupLayoutTrangChu();
        }else if (curr instanceof Admin_Quanly_Loai_SP_fg){
            setupLayoutTrangQLLSP((Admin_Quanly_Loai_SP_fg) curr);
        } else if (curr instanceof  Admin_QuanLy_Loai_SPCon_fg) {
            setupLayoutTrangQLLSPCon((Admin_QuanLy_Loai_SPCon_fg)curr);
        } else if (curr instanceof AdminQuanLyThuongHieu_fg) {
            setupLayoutTrangQLThuongHieu((AdminQuanLyThuongHieu_fg)curr);
        } else if (curr instanceof Admin_QuanLy_SP_fg) {
            setupLayoutTrangQLSP((Admin_QuanLy_SP_fg) curr);
        }
    }

    private void setupLayoutTrangQLThuongHieu(AdminQuanLyThuongHieu_fg fragment) {
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu1);
        binding.add.setOnClickListener(v -> {
            createThemTHDialog(fragment);
        });
    }
    private void setupLayoutTrangQLLSP(Admin_Quanly_Loai_SP_fg fragment) {
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu1);
        binding.add.setOnClickListener(v -> {
            createThemLSPDialog(fragment);
        });
    }
    private void setupLayoutTrangQLLSPCon(Admin_QuanLy_Loai_SPCon_fg fragment) {
        binding.add.hide();
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu1);
    }
    private void setupLayoutTrangQLSP(Admin_QuanLy_SP_fg fragment) {
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu1);
        binding.add.setOnClickListener(v -> {
            createThemSanPhamDialog( fragment);
        });
    }
    private void setupLayoutTrangChu() {
        binding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu2);
        ///đẻ đây tí làm

        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.baseline_account_circle_24);
        binding.add.setImageDrawable(drawable);
        binding.add.setOnClickListener(v -> {
            if (binding.main.isDrawerOpen(GravityCompat.END)) {
                binding.main.closeDrawer(GravityCompat.END); // Đóng NavigationView
            } else {
                binding.main.openDrawer(GravityCompat.END); // Mở NavigationView
            }
        });
    }






    private void createThemLSPDialog(Admin_Quanly_Loai_SP_fg fragment){
        View v = LayoutInflater.from(Admin_screen.this).inflate(R.layout.dialog_edit_tenlssp,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_screen.this);
        builder.setView(v);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.setCancelable(false);
        EditText edtTenLSP= v.findViewById(R.id.txtedtTensp);
        AppCompatButton btnsua=v.findViewById(R.id.btnsua);
        AppCompatButton btnhuy=v.findViewById(R.id.btnhuy);
        btnsua.setOnClickListener(v1 -> {
            String getten=edtTenLSP.getText().toString();
            if(getten.trim().equals("")){
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thông tin không hợ lệ!",
                        "Vui lòng nhập thông tin!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
            }
            else {
                LoaiSPEndpoint loaiSPEndpoint = new LoaiSPEndpoint();
                loaiSPEndpoint.addLoaiSanPham(new LoaiSP(edtTenLSP.getText().toString(), 0), new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            fragment.refresh();
                            MotionToast.Companion.createToast(Admin_screen.this,
                                    "Đã thêm!",
                                    "Đã thêm thành công!",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                            dialog.dismiss();
                        }else {
                            if(response.code()==409){
                                MotionToast.Companion.createToast(Admin_screen.this,
                                        "Thêm thất bại!",
                                        "Tên đã tồn tại",
                                        MotionToastStyle.WARNING,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                            }else {
                                MotionToast.Companion.createToast(Admin_screen.this,
                                        "Thêm thất bại!",
                                        "Có lỗi xảy ra",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        MotionToast.Companion.createToast(Admin_screen.this,
                                "Thêm thất bại!",
                                "Không thể kết nối với máy chủ",
                                MotionToastStyle.NO_INTERNET,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    }
                });


            }
        });
        btnhuy.setOnClickListener(v1 -> {
            dialog.dismiss();
        });

        dialog.show();
    }
    private void createThemTHDialog(AdminQuanLyThuongHieu_fg fragment) {
        View v = LayoutInflater.from(this).inflate(R.layout.add_thuong_hieu_dialog,null);

        AppCompatButton btnsua=v.findViewById(R.id.btnsua);
        AppCompatButton btnhuy=v.findViewById(R.id.btnhuy);
        ImageView im= v.findViewById(R.id.anh);
        ProgressBar progressBar = v.findViewById(R.id.progress);
        Glide.with(progressBar).load(R.drawable.loading);
        EditText edtTenThuongHieu= v.findViewById(R.id.txtTenThuongHieu);

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_screen.this);
        builder.setView(v);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        final Uri[] uri1 = {null};
        im.setOnClickListener(v1 -> {
            if(RequestPermission.request(this, Manifest.permission.READ_EXTERNAL_STORAGE,1)){
                imagePicker.pickImage(new ImagePicker.ImagePickerCallback() {
                    @Override
                    public void onImagePicked(Uri uri) {
                        im.setImageURI(uri);
                        uri1[0] =uri;
                    }

                    @Override
                    public void onPickCancelled() {
                        Toast.makeText(Admin_screen.this, "Đã huỷ", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this, "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
            }
        });
        btnsua.setOnClickListener(v1 -> {
            ///kiểm tra nhập chưa
            if(edtTenThuongHieu.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thông tin thiếu!",
                        "Vui lòng nhập đủ thông tin.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            ///kiểm tra chọn ảnh chưa
            if(uri1[0]==null){
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thông tin thiếu!",
                        "Vui lòng chọn ảnh.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            ///bắt đầu upload
            ThuongHieuEndpoint thuongHieuEndpoint = new ThuongHieuEndpoint();
            progressBar.setVisibility(View.VISIBLE);
            thuongHieuEndpoint.createThuongHieu(edtTenThuongHieu.getText().toString().trim(), uri1[0], this, new ThuongHieuEndpoint.CreateThuongHieuCallBack() {
                @Override
                public void onSuccess() {
                    MotionToast.Companion.createToast(Admin_screen.this,
                            "Thành công!",
                            "Đã thêm thành công.",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    fragment.resetData();
                }
                @Override
                public void onLoi(String message) {
                    MotionToast.Companion.createToast(Admin_screen.this,
                            "Thất bại!",
                            "Thêm thất bại "+message,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    Toast.makeText(Admin_screen.this, "Thêm thất bại. "+ message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                }
                @Override
                public void onTrungTen() {
                    MotionToast.Companion.createToast(Admin_screen.this,
                            "Thất bại!",
                            "Tên đã tồn tại",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onSave() {
                    MotionToast.Companion.createToast(Admin_screen.this,
                            "Đang lưu",
                            "",
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });



        });
        btnhuy.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
    }
    private void createThemSanPhamDialog(Admin_QuanLy_SP_fg fragment){
        ThuongHieuEndpoint thuongHieuEndpoint = new ThuongHieuEndpoint();
        thuongHieuEndpoint.getAllThuongHieu(thuongHieus -> {
            createViewAddSPDialog(thuongHieus,fragment);

        });
    }
    ///for createThemSanPhamDialog
    private void createViewAddSPDialog(@NonNull List<ThuongHieu> list,Admin_QuanLy_SP_fg fragment){
        View v = LayoutInflater.from(Admin_screen.this).inflate((R.layout.admin_add_sp_dialog),null);
        EditText txttensanpham = v.findViewById(R.id.txttensanpham);
        EditText txtgia = v.findViewById(R.id.txtgia);
        EditText txtmota = v.findViewById(R.id.txtmota);
        Spinner spnThuonghieu = v.findViewById(R.id.spnThuonghieu);
        Switch swisInMainScreen = v.findViewById(R.id.swisInMainScreen);
        Switch swisisActivate = v.findViewById(R.id.swisisActivate);
        Button add = v.findViewById(R.id.them);
        Button cancel = v.findViewById(R.id.huy);

        ImageView addanh = v.findViewById(R.id.addanh);
        ImageView progressBar = v.findViewById(R.id.progress);
        Glide.with(progressBar).load(R.drawable.loading).into(progressBar);
        if(!list.isEmpty()) {
            ArrayAdapter<ThuongHieu> adapter = new ArrayAdapter<>(Admin_screen.this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spnThuonghieu.setAdapter(adapter);

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_screen.this);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        Uri[] uris ={null};
        addanh.setOnClickListener(v1 -> {
            if(!RequestPermission.request(Admin_screen.this,Manifest.permission.READ_EXTERNAL_STORAGE,1)) {
                Toast.makeText(this, "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
                return;
            }
            imagePicker.pickImage(new ImagePicker.ImagePickerCallback() {
                @Override
                public void onImagePicked(Uri uri) {
                    addanh.setImageURI(uri);
                    uris[0]=uri;
                }

                @Override
                public void onPickCancelled() {
                    Toast.makeText(Admin_screen.this, "Đã huỷ", Toast.LENGTH_SHORT).show();
                }
            });
        });


        add.setOnClickListener(v1 -> {
            if(txttensanpham.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thiếu thông tin!",
                        "Vui lòng điền tên sản phẩm!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));

                txttensanpham.requestFocus();
                return;
            }
            if(txtgia.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thiếu thông tin!",
                        "Vui lòng điền giá sản phẩm!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                txtgia.requestFocus();
                return;
            }
            if(txtmota.getText().toString().trim().equals("")){
                txtmota.requestFocus();
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thiếu thông tin!",
                        "Vui lòng điền mô tả!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));

            }
            String regex = "^[1-9][0-9]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(txtgia.getText().toString().trim());
            if(!matcher.matches()){
                txtgia.requestFocus();
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Nhập sai!",
                        "Giá tiền không hợp lệ!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }

            if(uris[0]==null){
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thiếu thông tin!",
                        "Vui lòng chọn ảnh cho sản phẩm.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            String[] urls={null};
            CloudinaryUpload upload = new CloudinaryUpload(Admin_screen.this);
            progressBar.setVisibility(View.VISIBLE);
            upload.uploadImage(uris[0], new CloudinaryUpload.UploadCallback() {
                @Override
                public void onUploadSuccess(String imageUrl) {
                    urls[0]= imageUrl;
                    SanPham sanPham = new SanPham(
                            txttensanpham.getText().toString()
                            ,Integer.parseInt(txtgia.getText().toString())
                            ,txtmota.getText().toString()
                            ,(ThuongHieu)spnThuonghieu.getSelectedItem()
                            ,swisisActivate.isChecked() ? 1:0
                            ,swisInMainScreen.isChecked()? 1:0
                            ,imageUrl.replaceFirst("http://","https://")
                            );
                    uploadToDB(sanPham,dialog,progressBar,fragment);

                }

                @Override
                public void onUploadFailed() {
                    MotionToast.Companion.createToast(Admin_screen.this,
                            "Lỗi!",
                            "Không thể kết nối đến máy chủ",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });

        });
    }

    private void uploadToDB(SanPham sanPham, Dialog dialog, ImageView progressBar,Admin_QuanLy_SP_fg fragment) {
        SanPhamEndpoint sanPhamEndpoint = new SanPhamEndpoint();
        sanPhamEndpoint.addSanPham(sanPham, new SanPhamEndpoint.CreateSanPhamCallback() {
            @Override
            public void onSuccess() {

                MotionToast.Companion.createToast(Admin_screen.this,
                        "Thành công!",
                        "Đã lưu thành công",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                fragment.resetLayout();
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }

            @Override
            public void onTrungten() {
                Toast.makeText(Admin_screen.this, "Tên sản phẩm đã tồn tại", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String message) {
                MotionToast.Companion.createToast(Admin_screen.this,
                        "Lỗi!",
                        message,
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(Admin_screen.this, www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}