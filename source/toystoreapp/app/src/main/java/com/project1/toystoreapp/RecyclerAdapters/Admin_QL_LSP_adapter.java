package com.project1.toystoreapp.RecyclerAdapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project1.toystoreapp.API_end_points.LoaiSPConEndpoint;
import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.LoaiSPCon;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Admin_QL_LSP_adapter extends RecyclerView.Adapter<Admin_QL_LSP_adapter.ViewHolder>{
    Context context;
    List<LoaiSP> list;

    public Admin_QL_LSP_adapter(Context context, List<LoaiSP> list) {
        this.context = context;
        this.list = list;
    }
    LoaiSPEndpoint loaiSPEndpoint = new LoaiSPEndpoint();
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_qllsp_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSP loaiSP = list.get(position);
        String tenloai = loaiSP.getTenloai();
        holder.tenLoaiSP.setText(tenloai.isEmpty() ? "Lỗi, hãy liên hệ với dev để giải quyết" : tenloai);
        holder.sw.setChecked(loaiSP.isActivate());
        holder.sw.setText((loaiSP.isActivate())?"Đang hiện trên trang chủ":"Đang ẩn khỏi trang chủ");
        holder.sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.sw.setChecked(!holder.sw.isChecked());
        });
        holder.sw.setOnClickListener(v -> {
            createActivateDialog(loaiSP,position);
        });
        holder.sua.setOnClickListener(v -> {
            createSuaDialog(loaiSP,position);
        });
        holder.xoa.setOnClickListener(v -> {
            createDeleteDialog(loaiSP,position);
        });
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(()->{
            loaiSPEndpoint.getSoLuongByID(loaiSP.getId(), new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful()){
                        holder.SL.setText("Gồm "+response.body()+" danh mục");
                        Log.e("ok ", response.body()+loaiSP.getId());
                    }else {
                        holder.SL.setText("Gồm 0 danh mục");
                        try {
                            Log.e("false: ", response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e("Error", "Request failed: " + call.request().url().toString());
                    Log.e("Error", "Exception message: " + t.getMessage());
                    Log.e("Error", "Stack trace: ", t);  // In toàn bộ stack trace

                    // Nếu bạn muốn hiển thị một giá trị mặc định
                    holder.SL.setText("0fghj");

                }
            });

        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    private void createActivateDialog(LoaiSP loaiSP, int posistion){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String TTstatus=loaiSP.isActivate()?"Ẩn khỏi":"Hiển thị lên";
        builder.setTitle(TTstatus+" trang chủ");
        String status= loaiSP.isActivate()?"ẩn sản phẩm khỏi":"hiện sản phẩm lên";
        builder.setMessage("Bạn sẽ "+status+" trang chủ");
        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            LoaiSPEndpoint loaiSPEndpoint = new LoaiSPEndpoint();
            loaiSPEndpoint.activateToggle(loaiSP, new Callback<LoaiSP>() {
                @Override
                public void onResponse(Call<LoaiSP> call, Response<LoaiSP> response) {
                    if(response.isSuccessful()){
                        list.set(posistion,response.body());

                        notifyItemChanged(posistion);
                    }else {
                        MotionToast.Companion.createToast((Activity) context,
                                "Thất bại!",
                                "Có lỗi xảy ra.",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                    }
                }
                @Override
                public void onFailure(Call<LoaiSP> call, Throwable t) {
                    MotionToast.Companion.createToast((Activity) context,
                            "Thất bại!",
                            "Không thể kết nối với máy chủ.",
                            MotionToastStyle.NO_INTERNET,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));

                }
            });
            dialog.dismiss();
        }));
        builder.setNegativeButton("huỷ",((dialog, which) -> {
            dialog.dismiss();
        }));
        AlertDialog dialog= builder.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void updateData(List<LoaiSP> newList){
        this.list=newList;
        notifyDataSetChanged();
    }
    private void createSuaDialog(LoaiSP loaiSP,int posistion){
        LoaiSP loaiSPtmp=new LoaiSP(loaiSP);
        View v =LayoutInflater.from(context).inflate(R.layout.dialog_edit_tenlssp,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.setCancelable(false);
        EditText edtTenLSP= v.findViewById(R.id.txtedtTensp);
        edtTenLSP.setText(loaiSP.getTenloai());
        AppCompatButton btnsua=v.findViewById(R.id.btnsua);
        AppCompatButton btnhuy=v.findViewById(R.id.btnhuy);
        btnsua.setOnClickListener(v1 -> {
            String getten=edtTenLSP.getText().toString();
            if(getten.trim().equals("")){
                MotionToast.Companion.createToast((Activity) context,
                        "Thiếu thông tin!",
                        "Vui lòng nhập đủ thông tin.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
            }
            else if(getten.equals(loaiSP.getTenloai())){
                MotionToast.Companion.createToast((Activity) context,
                        "Cảnh báo!",
                        "Thông tin chưa được thay đổi.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));

            }
            else {
                loaiSPtmp.setTenloai(getten);
                LoaiSPEndpoint loaiSPEndpoint = new LoaiSPEndpoint();
                loaiSPEndpoint.editLSP(loaiSPtmp, new Callback<>() {
                    @Override
                    public void onResponse(Call<LoaiSP> call, Response<LoaiSP> response) {
                        if(response.isSuccessful()){
                            try {
                                list.set(posistion,response.body());
                                notifyDataSetChanged();
                                MotionToast.Companion.createToast((Activity) context,
                                        "Thành công!",
                                        "Sửa thành công.",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            MotionToast.Companion.createToast((Activity) context,
                                    "Thất bại!",
                                    "Sửa thất bại.",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                            try {
                                Log.e(TAG, "onResponse: "+ response.errorBody().string());

                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoaiSP> call, Throwable t) {
                        MotionToast.Companion.createToast((Activity) context,
                                "Thất bại!",
                                "Không thể kết nối với máy chủ.",
                                MotionToastStyle.NO_INTERNET,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                        Log.e("onFailure: ",t.getMessage() );
                    }
                });
                dialog.dismiss();
            }
        });
        btnhuy.setOnClickListener(v1 -> {
            dialog.dismiss();
        });

        dialog.show();

    }
    private void createDeleteDialog(LoaiSP loaiSP, int posistion){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String alert = "<font color='#FF0000'>Lưu ý, việc xoá sẽ loại bỏ loại sản phẩm này vĩnh viễn</font>";
        String message ="Bạn sẽ xoá danh mục <b>"+loaiSP.getTenloai()+"</b><br>"+alert;
        builder.setMessage(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            loaiSPEndpoint.deleteLoaiSP(loaiSP, new LoaiSPEndpoint.onDeleteAwait() {
                @Override
                public void onSuccess() {
                    MotionToast.Companion.createToast((Activity) context,
                            "Thành công!",
                            "Đã xoá thành công.",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                    list.remove(posistion);
                    notifyItemRangeChanged(posistion,list.size());
                }

                @Override
                public void onFailure(String errorr) {
                    MotionToast.Companion.createToast((Activity) context,
                            "Thất bại!",
                            errorr,
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });
        }));


        builder.setNegativeButton("huỷ",((dialog, which) -> {
            dialog.dismiss();
        }));
        AlertDialog dialog= builder.create();
        dialog.getWindow().setDimAmount(0.8f);
        dialog.setCancelable(false);
        dialog.show();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenLoaiSP;
        TextView SL;
        Switch sw;
        Button sua;
        Button xoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenLoaiSP = itemView.findViewById(R.id.tvTen);
            SL = itemView.findViewById(R.id.tvSl);
            sw= itemView.findViewById(R.id.SWactivate);
            sua=itemView.findViewById(R.id.btnSua);
            xoa = itemView.findViewById(R.id.xoa);
        }
    }
}