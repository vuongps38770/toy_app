package com.project1.toystoreapp.layout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.API_end_points.SanPhamEndpoint;
import com.project1.toystoreapp.API_end_points.ThuongHieuEndpoint;
import com.project1.toystoreapp.Activities.Admin_screen;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.GridSpacingItemDecoration;
import com.project1.toystoreapp.Classes.ImagePicker;
import com.project1.toystoreapp.Classes.RequestPermission;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.RecyclerAdapters.Admin_QL_SP_adapter;
import com.project1.toystoreapp.databinding.FragmentAdminQuanlySpFgBinding;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.SanPham;
import com.project1.toystoreapp.model.ThuongHieu;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Admin_QuanLy_SP_fg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Admin_QuanLy_SP_fg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Admin_QuanLy_SP_fg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Admin_QuanLy_SP_fg.
     */
    // TODO: Rename and change types and number of parameters
    public static Admin_QuanLy_SP_fg newInstance(String param1, String param2) {
        Admin_QuanLy_SP_fg fragment = new Admin_QuanLy_SP_fg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker= new ImagePicker(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Admin_QL_SP_adapter adapter;
    ImagePicker imagePicker;
    FragmentAdminQuanlySpFgBinding binding;
    SanPhamEndpoint sanPhamEndpoint;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminQuanlySpFgBinding.inflate(inflater,container,false);
        sanPhamEndpoint = new SanPhamEndpoint();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resetLayout();

        binding.searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    }
    public void search(boolean isVisibleSearchBar){
        if(isVisibleSearchBar){
            showSearchViewWithAnimation(binding.searchbar);
            binding.searchbar.setFocusableInTouchMode(true);
            binding.searchbar.setBackgroundColor(Color.WHITE);
        }
        ImageView closeButton = binding.searchbar.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            binding.searchbar.setQuery("", false);
            binding.searchbar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "hide", Toast.LENGTH_SHORT).show();
            binding.searchbar.setBackgroundColor(Color.TRANSPARENT);
        });
    }
    private void showSearchViewWithAnimation(SearchView searchView) {
        searchView.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(
                0, 0, -100, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        searchView.startAnimation(animation);
    }
    public void resetLayout(){
        sanPhamEndpoint.getAllSP(list -> {
            if(list.isEmpty()){
                getActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                });
            }else {
                getActivity().runOnUiThread(()->{
                    adapter = new Admin_QL_SP_adapter(getContext(),list);
                    adapter.setItemClicklistener((sanPham, posistion) -> {
                        createViewEditSPDialog(sanPham, posistion);
                    });
                    binding.recyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));
                    binding.recyclerview.setHasFixedSize(true);
                    binding.recyclerview.setAdapter(adapter);
                });
            }
        });
    }
    private void createViewEditSPDialog(SanPham sanPham, int posistion){
        View v = LayoutInflater.from(getContext()).inflate((R.layout.admin_add_sp_dialog),null);
        EditText txttensanpham = v.findViewById(R.id.txttensanpham);
        EditText txtgia = v.findViewById(R.id.txtgia);
        EditText txtmota = v.findViewById(R.id.txtmota);
        Spinner spnThuonghieu = v.findViewById(R.id.spnThuonghieu);
        Switch swisInMainScreen = v.findViewById(R.id.swisInMainScreen);
        Switch swisisActivate = v.findViewById(R.id.swisisActivate);
        getActivity().runOnUiThread(()->{
            ThuongHieuEndpoint thuongHieuEndpoint = new ThuongHieuEndpoint();
            thuongHieuEndpoint.getAllThuongHieuActivated(list->{
                if(list.isEmpty()) return;
                ArrayAdapter<ThuongHieu> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,list);
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                spnThuonghieu.setAdapter(adapter);
                for (int i = 0;i<list.size();i++){
                    Log.e("createViewEditSPDialog: ", list.get(i).getId()+"---main "+sanPham.getThuonghieu().getId());
                    Log.e("createViewEditSPDialog: ", list.get(i).getTenthuonghieu()+"---main "+sanPham.getThuonghieu().getTenthuonghieu());
                    if(sanPham.getThuonghieu().getId().equals(list.get(i).getId())){
                        spnThuonghieu.setSelection(i);
                    }
                }
            });
        });
        swisisActivate.setChecked(sanPham.getIsActivate()==1);
        swisInMainScreen.setChecked(sanPham.getIsInMainScreen()==1);
        swisisActivate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!swisisActivate.isChecked()){
                swisInMainScreen.setChecked(false);
            }
        });
        swisInMainScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!swisisActivate.isChecked()){
                swisInMainScreen.setChecked(false);
            }
        });

        Button add = v.findViewById(R.id.them);
        Button cancel = v.findViewById(R.id.huy);
        ImageView addanh = v.findViewById(R.id.addanh);
        Glide.with(addanh)
                .load(sanPham.getUrlanh())
                .error(R.drawable.img_1)
                .into(addanh);
        ImageView progressBar = v.findViewById(R.id.progress);
        Glide.with(progressBar).load(R.drawable.loading).into(progressBar);
        txtgia.setText(""+sanPham.getGia());
        txtmota.setText(""+sanPham.getMota());
        txttensanpham.setText(sanPham.getTensanpham());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
        cancel.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        Uri[] uris ={null};
        addanh.setOnClickListener(v1 -> {
            if(!RequestPermission.request(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,1)) {
                Toast.makeText(getContext(), "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Đã huỷ", Toast.LENGTH_SHORT).show();
                }
            });
        });
        add.setOnClickListener(v1 -> {
            if(txttensanpham.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast(getActivity(),
                        "Thiếu thông tin!",
                        "Vui lòng điền tên sản phẩm",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
                txttensanpham.requestFocus();
                return;
            }
            if(txtgia.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast(getActivity(),
                        "Thiếu thông tin!",
                        "Vui lòng nhập giá!",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
                txtgia.requestFocus();
                return;
            }
            if(txtmota.getText().toString().trim().equals("")){
                txtmota.requestFocus();
                MotionToast.Companion.createToast(getActivity(),
                        "Thiếu thông tin!",
                        "Vui lòng nhập mô tả.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
                return;
            }
            String regex = "^[1-9][0-9]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(txtgia.getText().toString().trim());
            if(!matcher.matches()){
                txtgia.requestFocus();
                MotionToast.Companion.createToast(getActivity(),
                        "Sai thông tin!",
                        "Vui lòng nhập đúng giá",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
                return;
            }
            if(uris[0]!=null){
                CloudinaryUpload upload = new CloudinaryUpload(getActivity());
                progressBar.setVisibility(View.VISIBLE);
                upload.uploadImage(uris[0], new CloudinaryUpload.UploadCallback() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        SanPham sanPhamnew = new SanPham(
                                txttensanpham.getText().toString()
                                ,Integer.parseInt(txtgia.getText().toString())
                                ,txtmota.getText().toString()
                                ,sanPham.getThuonghieu()
                                ,swisisActivate.isChecked() ? 1:0
                                ,swisInMainScreen.isChecked()? 1:0
                                ,imageUrl.replaceFirst("http://","https://")
                        );
                        sanPhamnew.setId(sanPham.getId());
                        uploadToDB(sanPhamnew,dialog,progressBar,posistion);
                    }

                    @Override
                    public void onUploadFailed() {
                        MotionToast.Companion.createToast(getActivity(),
                                "Đã xảy ra vấn đề!",
                                "vui lòng kiểm tra kết nối",
                                MotionToastStyle.NO_INTERNET,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }else {
                SanPham sanPhamnew = new SanPham(
                        txttensanpham.getText().toString()
                        ,Integer.parseInt(txtgia.getText().toString())
                        ,txtmota.getText().toString()
                        ,((ThuongHieu) spnThuonghieu.getSelectedItem())
                        ,swisisActivate.isChecked() ? 1:0
                        ,swisInMainScreen.isChecked()? 1:0
                        ,sanPham.getUrlanh()
                );
                sanPhamnew.setId(sanPham.getId());
                Log.e("createViewEditSPDialog: ",  ((ThuongHieu) spnThuonghieu.getSelectedItem()).getTenthuonghieu());
                uploadToDB(sanPhamnew,dialog,progressBar,posistion);
            }


        });
    }

    private void uploadToDB(SanPham newsanPham, Dialog dialog, ImageView progressBar, int posistion) {
        sanPhamEndpoint.editSP(newsanPham, new SanPhamEndpoint.EditSanPhamCallback() {
            @Override
            public void onSuccess(SanPham sanPham) {
                getActivity().runOnUiThread(()->{
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(posistion,sanPham);
                    resetLayout();
                });


                MotionToast.Companion.createToast(getActivity(),
                        "Thành công!",
                        "Đã sửa thành công",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                MotionToast.Companion.createToast(getActivity(),
                        "Thất bại!",
                        message,
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
    }
}