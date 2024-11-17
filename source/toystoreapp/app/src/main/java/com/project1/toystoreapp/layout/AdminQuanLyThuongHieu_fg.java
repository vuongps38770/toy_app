package com.project1.toystoreapp.layout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project1.toystoreapp.API_end_points.ThuongHieuEndpoint;
import com.project1.toystoreapp.Activities.Admin_screen;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.GridSpacingItemDecoration;
import com.project1.toystoreapp.Classes.ImagePicker;
import com.project1.toystoreapp.Classes.RequestPermission;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.RecyclerAdapters.Admin_QL_ThuongHieu_adapter;
import com.project1.toystoreapp.databinding.FragmentAdminQuanLyThuongHieuFgBinding;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.ThuongHieu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminQuanLyThuongHieu_fg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminQuanLyThuongHieu_fg extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminQuanLyThuongHieu_fg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminQuanLyThuongHieu_fg.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminQuanLyThuongHieu_fg newInstance(String param1, String param2) {
        AdminQuanLyThuongHieu_fg fragment = new AdminQuanLyThuongHieu_fg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        uploader = new CloudinaryUpload(getContext());
        imagePicker = new ImagePicker(this);

    }
    FragmentAdminQuanLyThuongHieuFgBinding binding;
    List<ThuongHieu> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminQuanLyThuongHieuFgBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resetData();
            }
        });
    }
    ThuongHieuEndpoint thuongHieuEndpoint = new ThuongHieuEndpoint();
    CloudinaryUpload uploader;
    ImagePicker imagePicker;
    public void resetData(){
        thuongHieuEndpoint.getAllThuongHieu(list->{
            if(list.isEmpty()){
                Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                return;
            }else {
                this.list=list;
            }
            Admin_QL_ThuongHieu_adapter adapter = new Admin_QL_ThuongHieu_adapter(getContext(), list);
            adapter.setListener((thuongHieu, at) -> {
                creatSuaTenThuongHieuDialog(thuongHieu,at,adapter);
            });
            binding.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
            binding.recyclerview.addItemDecoration(new GridSpacingItemDecoration(2,50,false));
            binding.recyclerview.setAdapter(adapter);
        });

    }

    private void creatSuaTenThuongHieuDialog(ThuongHieu thuongHieu,int pos,Admin_QL_ThuongHieu_adapter adapter) {
        Uri[] uriholder = {null};
        ThuongHieu[] thuongHieuHolder={null};
        View v = LayoutInflater.from(getContext()).inflate(R.layout.add_thuong_hieu_dialog,null);
        AppCompatButton btnsua=v.findViewById(R.id.btnsua);
        btnsua.setText("xác nhận");
        AppCompatButton btnhuy=v.findViewById(R.id.btnhuy);
        ImageView im= v.findViewById(R.id.anh);
        ProgressBar progressBar = v.findViewById(R.id.progress);
        EditText edtTenThuongHieu= v.findViewById(R.id.txtTenThuongHieu);
        edtTenThuongHieu.setText(thuongHieu.getTenthuonghieu());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(v);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        Glide.with(im)
                .load(thuongHieu.getUrlthumbnail())
                .placeholder(R.drawable.img_1)
                .error(R.drawable.img_1)
                .into(im);

        im.setOnClickListener(v1 -> {
            if(RequestPermission.request(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE,1)){
                imagePicker.pickImage(new ImagePicker.ImagePickerCallback() {
                    @Override
                    public void onImagePicked(Uri uri) {
                        im.setImageURI(uri);
                        uriholder[0] =uri;
                    }

                    @Override
                    public void onPickCancelled() {
                        Toast.makeText(getContext(), "Đã huỷ", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getContext(), "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
            }
        });
        btnhuy.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        btnsua.setOnClickListener(v1 -> {
            ///Trường tên trống+bảo trì thêm các trường khác
            if(edtTenThuongHieu.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast(getActivity(),
                        "Thiếu thông tin!",
                        "Thông tin không được bỏ trống.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            ///Không thêm thông tin
            if(uriholder[0]==null&&edtTenThuongHieu.getText().toString().trim().equals(thuongHieu.getTenthuonghieu())){
                MotionToast.Companion.createToast(getActivity(),
                        "Thiếu thông tin!",
                        "Chưa có thông tin nào được chỉnh sửa.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            ///sửa ít nhất 1 thông tin
            progressBar.setVisibility(View.VISIBLE);
            ///sửa tên ko sửa ảnh
            if(uriholder[0]==null){
                thuongHieu.setTenthuonghieu(edtTenThuongHieu.getText().toString());
                addToDb(thuongHieu,pos,thuongHieuHolder,adapter,dialog,progressBar);
            }
            ///sửa cả 2
            else {
                uploader.uploadImage(uriholder[0], new CloudinaryUpload.UploadCallback() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        thuongHieu.setTenthuonghieu(edtTenThuongHieu.getText().toString());
                        thuongHieu.setUrlthumbnail(imageUrl.replaceFirst("http://","https://"));
                        addToDb(thuongHieu,pos,thuongHieuHolder,adapter,dialog,progressBar);
                    }

                    @Override
                    public void onUploadFailed() {
                        MotionToast.Companion.createToast(getActivity(),
                                "Lỗi!",
                                "Có vấn đề xảy ra trong quá trình upload hình ảnh.",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                    }
                });
            }

        });
    }
    private void addToDb(ThuongHieu thuongHieu,int pos,ThuongHieu[] thuongHieuHolder,Admin_QL_ThuongHieu_adapter adapter,AlertDialog dialog, ProgressBar progress){
        thuongHieuEndpoint.editThuongHieu(thuongHieu, new Callback<ThuongHieu>() {
            @Override
            public void onResponse(Call<ThuongHieu> call, Response<ThuongHieu> response) {
                if(response.isSuccessful()){
                    thuongHieuHolder[0]=response.body();
                    adapter.notifyItemChanged(pos,thuongHieu);
                    MotionToast.Companion.createToast(getActivity(),
                            "Thành công!",
                            "Đã sửa thành công.",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                    progress.setVisibility(View.GONE);
                    dialog.dismiss();

                }else {
                    MotionToast.Companion.createToast(getActivity(),
                            "Lỗi!",
                            "Có vấn đề xảy ra, sửa thất bại.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                    progress.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ThuongHieu> call, Throwable t) {
                MotionToast.Companion.createToast(getActivity(),
                        "Lỗi!",
                        "Không thể kết nối với máy chủ.",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(getContext(), www.sanju.motiontoast.R.font.helvetica_regular));
                progress.setVisibility(View.GONE);

            }
        });
    }
}