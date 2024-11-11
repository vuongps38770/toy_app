package com.project1.toystoreapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.API_end_points.SanPhamEndpoint;
import com.project1.toystoreapp.API_end_points.ThuongHieuEndpoint;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.ImagePicker;
import com.project1.toystoreapp.Classes.RequestPermission;
import com.project1.toystoreapp.Classes.ThuongHieu;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.ActivityAdminScreenBinding;
import com.project1.toystoreapp.layout.AdminQuanLyThuongHieu_fg;
import com.project1.toystoreapp.layout.Admin_QuanLy_Loai_SPCon_fg;
import com.project1.toystoreapp.layout.Admin_QuanLy_SP_fg;
import com.project1.toystoreapp.layout.Admin_QuanLy_all_fg;
import com.project1.toystoreapp.layout.Admin_Quanly_Loai_SP_fg;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.SanPham;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_screen extends AppCompatActivity {
    ImagePicker imagePicker;
    private ActivityAdminScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = new ImagePicker(this);
        binding = ActivityAdminScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Fragment fragment = new Admin_QuanLy_all_fg();
        setFragment(fragment,false);
        getSupportFragmentManager().addOnBackStackChangedListener(this::settupCurrentFG);
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){
                    setFragment(new Admin_QuanLy_all_fg(),false);
                    getSupportFragmentManager().popBackStack();
                }else if (item.getItemId()==R.id.search){
                    if(getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof Admin_Quanly_Loai_SP_fg){
                        Toast.makeText(Admin_screen.this, "searrchq", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
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
        Fragment curr = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
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
            createThemSPDialog(fragment);
        });
    }
    private void setupLayoutTrangQLLSPCon(Admin_QuanLy_Loai_SPCon_fg fragment) {
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu1);
    }
    private void setupLayoutTrangQLSP(Admin_QuanLy_SP_fg fragment) {
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu1);
        binding.add.setOnClickListener(v -> {
            createThemSanPhamDialog();
        });
    }
    private void setupLayoutTrangChu() {
        binding.bottomNav.getMenu().clear();
        binding.bottomNav.inflateMenu(R.menu.bottom_admin_menu2);
        ///đẻ đây tí làm
        binding.add.setOnClickListener(null);
    }

    private void createThemSPDialog(Admin_Quanly_Loai_SP_fg fragment){
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
                Toast.makeText(Admin_screen.this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
            }
            else {
                LoaiSPEndpoint loaiSPEndpoint = new LoaiSPEndpoint();
                loaiSPEndpoint.addLoaiSanPham(new LoaiSP(edtTenLSP.getText().toString(), 0), new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            fragment.refresh();
                            Toast.makeText(Admin_screen.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            if(response.code()==409){
                                Toast.makeText(Admin_screen.this, "Thêm thất bại, đã tồn tại", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Admin_screen.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(Admin_screen.this, "Thêm thất bại, vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
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
        EditText edtTenThuongHieu= v.findViewById(R.id.txtTenThuongHieu);

        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_screen.this);
        builder.setView(v);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setDimAmount(0.8f);
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
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            ///kiểm tra chọn ảnh chưa
            if(uri1[0]==null){
                Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                return;
            }
            ///bắt đầu upload
            ThuongHieuEndpoint thuongHieuEndpoint = new ThuongHieuEndpoint();
            progressBar.setVisibility(View.VISIBLE);
            thuongHieuEndpoint.createThuongHieu(edtTenThuongHieu.getText().toString().trim(), uri1[0], this, new ThuongHieuEndpoint.CreateThuongHieuCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(Admin_screen.this, "Đã thêm thành công", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                }
                @Override
                public void onLoi(String message) {
                    Toast.makeText(Admin_screen.this, "Thêm thất bại. "+ message, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                }
                @Override
                public void onTrungTen() {
                    Toast.makeText(Admin_screen.this, "Tên đã tồn tại", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onSave() {
                    Toast.makeText(Admin_screen.this, "Đang lưu", Toast.LENGTH_SHORT).show();
                }
            });



        });
        btnhuy.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
    }
    private void createThemSanPhamDialog(){
        ThuongHieuEndpoint thuongHieuEndpoint = new ThuongHieuEndpoint();
        thuongHieuEndpoint.getAllThuongHieu(thuongHieus -> {
            Toast.makeText(Admin_screen.this, String.valueOf(thuongHieus.size()), Toast.LENGTH_SHORT).show();
            createViewDialog(thuongHieus);

        });
    }
    private void createViewDialog(List<ThuongHieu> list){
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
        ProgressBar progressBar = v.findViewById(R.id.progress);
        if(!list.isEmpty()) {
            ArrayAdapter<ThuongHieu> adapter = new ArrayAdapter<>(Admin_screen.this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spnThuonghieu.setAdapter(adapter);

        }



        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_screen.this);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
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
            if(uris[0]==null){
                Toast.makeText(this, "Vui lòng chọn ảnh cho sản phẩm", Toast.LENGTH_SHORT).show();
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
                    uploadToDB(sanPham,dialog,progressBar);

                }

                @Override
                public void onUploadFailed() {
                    Toast.makeText(Admin_screen.this, "Đã xảy ra vấn đề, vui lòng kiểm tra kết nối", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    private void uploadToDB(SanPham sanPham, Dialog dialog, ProgressBar progressBar) {
        SanPhamEndpoint sanPhamEndpoint = new SanPhamEndpoint();
        sanPhamEndpoint.addSanPham(sanPham, new SanPhamEndpoint.CreateSanPhamCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(Admin_screen.this, "Đã lưu thành công", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Admin_screen.this, "Có lỗi xảy ra "+message, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}