package com.project1.toystoreapp.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project1.toystoreapp.API_end_points.LoaiSPConEndpoint;
import com.project1.toystoreapp.API_end_points.SanPhamEndpoint;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.ImagePicker;
import com.project1.toystoreapp.Classes.RequestPermission;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.RecyclerAdapters.Admin_QL_SP_adapter;
import com.project1.toystoreapp.databinding.ActivityAdminQlSpBinding;
import com.project1.toystoreapp.layout.Admin_QuanLy_SP_fg;
import com.project1.toystoreapp.model.LoaiSPCon;
import com.project1.toystoreapp.model.SanPham;
import com.project1.toystoreapp.model.ThuongHieu;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Admin_qlSP extends AppCompatActivity {
    LoaiSPCon loaiSPCon;
    private String ID="";
    ActivityAdminQlSpBinding binding;
    SanPhamEndpoint sanPhamEndpoint;
    List<SanPham> list;
    LoaiSPConEndpoint loaiSPConEndpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminQlSpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loaiSPCon = new LoaiSPCon();
        loaiSPConEndpoint = new LoaiSPConEndpoint();
        sanPhamEndpoint = new SanPhamEndpoint();
        Glide.with(binding.load).load(R.drawable.loading).into(binding.load);
        binding.load.setVisibility(View.GONE);

        list = new ArrayList<>();
        Intent intent = getIntent();
        loaiSPCon=(LoaiSPCon) intent.getSerializableExtra("SPCON");
        ID = loaiSPCon.getId();
        if(ID==null){
            return;
        }
        resetView();
    }

    private void resetView() {
        binding.load.setVisibility(View.VISIBLE);
        runOnUiThread(()->{
            sanPhamEndpoint.getAllsanphamByLoaiSPconID(ID, list->{
                this.list=list;
                if(list.isEmpty()) {
                    MotionToast.Companion.createToast(Admin_qlSP.this,
                            "Danh sachs trống",
                            "No data",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(Admin_qlSP.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }
                binding.load.setVisibility(View.GONE);
                Admin_QL_SP_adapter adapter = new Admin_QL_SP_adapter(this,list);
                binding.recyclerview.setLayoutManager(new GridLayoutManager(this,2));
                binding.recyclerview.setHasFixedSize(true);
                binding.recyclerview.setAdapter(adapter);
                adapter.setItemClicklistener((sanPham, posistion) -> {

                });
                adapter.setItemLongClicklistener((sanPham, posistion) -> {
                    createRemoveDialog(sanPham);
                });
            });
        });
        binding.btnadd.setOnClickListener(v -> {
            createAddSPDialog();

        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("SPCON",loaiSPCon);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loaiSPCon =(LoaiSPCon) savedInstanceState.getSerializable("SPCON");
    }
    private void createAddSPDialog() {
        View v =LayoutInflater.from(this).inflate(R.layout.admin_addsp_to_qlspcon_dialog,null);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        ImageView load = v.findViewById(R.id.load);
        ImageButton cancel = v.findViewById(R.id.cancel);
        SearchView search = v.findViewById(R.id.search);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v);
        AlertDialog dialog= builder.create();
        dialog.show();
        cancel.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        dialog.setCancelable(true);
        load.setVisibility(View.VISIBLE);
        sanPhamEndpoint.getAllsanphamNotHaveLoaiSPconID(ID, list->{
            load.setVisibility(View.GONE);
            Admin_QL_SP_adapter adapter = new Admin_QL_SP_adapter(this,list);
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.search(newText);
                    return true;
                }
            });

            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.setItemClicklistener((sanPham, posistion) -> {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Thêm sản phẩm này vào "+loaiSPCon.getTenloai());
                builder1.setMessage("Bạn sẽ thêm sản phẩm "+sanPham.getTensanpham()+" vào "+loaiSPCon.getTenloai());
                builder1.setPositiveButton("Thêm",(dialog1, which) -> {
                    loaiSPConEndpoint.addSP(ID, sanPham.getId(), new LoaiSPConEndpoint.onADDSP() {
                        @Override
                        public void onSuccess() {
                            dialog1.dismiss();
                            dialog.dismiss();
                            MotionToast.Companion.createToast(Admin_qlSP.this,
                                    "Thành công!",
                                    "Bạn đã thêm sản phẩm thành công!",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(Admin_qlSP.this, www.sanju.motiontoast.R.font.helvetica_regular));
                            resetView();
                        }

                        @Override
                        public void onFailure(String error) {
                            dialog1.dismiss();
                            MotionToast.Companion.createToast(Admin_qlSP.this,
                                    "Thất bại!",
                                    error,
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(Admin_qlSP.this, www.sanju.motiontoast.R.font.helvetica_regular));
                        }
                    });
                });
                builder1.setNegativeButton("Huỷ",(dialog1, which) -> {
                    dialog1.dismiss();
                });
                AlertDialog dialog1= builder1.create();
                dialog1.show();

            });
        });


    }
    private void createRemoveDialog(SanPham sanPham){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Xoá sản phẩm "+sanPham.getTensanpham()+" khỏi danh mục "+loaiSPCon.getTenloai());
        builder1.setMessage("Bạn sẽ xoá sản phẩm "+sanPham.getTensanpham()+" khỏi "+loaiSPCon.getTenloai());
        builder1.setPositiveButton("Xoá",(dialog1, which) -> {
            loaiSPConEndpoint.removeSP(ID, sanPham.getId(), new LoaiSPConEndpoint.onDeleteAwait(){
                @Override
                public void onSuccess() {
                    dialog1.dismiss();
                    MotionToast.Companion.createToast(Admin_qlSP.this,
                            "Thành công!",
                            "Bạn đã Xoá sản phẩm thành công!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_qlSP.this, www.sanju.motiontoast.R.font.helvetica_regular));
                    resetView();
                }

                @Override
                public void onFailure(String error) {
                    dialog1.dismiss();
                    MotionToast.Companion.createToast(Admin_qlSP.this,
                            "Thất bại!",
                            error,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(Admin_qlSP.this, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });
        });
        builder1.setNegativeButton("Huỷ",(dialog1, which) -> {
            dialog1.dismiss();
        });
        AlertDialog dialog1= builder1.create();
        dialog1.show();
    }
}