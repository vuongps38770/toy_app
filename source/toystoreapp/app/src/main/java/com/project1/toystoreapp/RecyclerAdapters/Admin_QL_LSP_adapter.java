package com.project1.toystoreapp.RecyclerAdapters;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.Interfaces.OnUpdateListener;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.ErrorRespond;
import com.project1.toystoreapp.model.LoaiSP;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_QL_LSP_adapter extends RecyclerView.Adapter<Admin_QL_LSP_adapter.ViewHolder>{
    Context context;
    List<LoaiSP> list;

    public Admin_QL_LSP_adapter(Context context, List<LoaiSP> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_qlsp_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSP loaiSP = list.get(position);
        String tenloai = loaiSP.getTenloai();
        holder.tenLoaiSP.setText(tenloai.isEmpty() ? "Lỗi, hãy liên hệ với dev để giải quyết" : tenloai);
        holder.sw.setChecked(loaiSP.isActivate());
        holder.sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.sw.setChecked(!holder.sw.isChecked());
        });
        holder.sw.setOnClickListener(v -> {
            createActivateDialog(loaiSP,position);
        });
        holder.sua.setOnClickListener(v -> {
            createSuaDialog(loaiSP,position);
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
                        Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoaiSP> call, Throwable t) {
                    Toast.makeText(context, "Vui lòng kiểm tra đường truyền", Toast.LENGTH_SHORT).show();

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
        AppCompatButton btnsua=v.findViewById(R.id.btnsua);
        AppCompatButton btnhuy=v.findViewById(R.id.btnhuy);
        btnsua.setOnClickListener(v1 -> {
            String getten=edtTenLSP.getText().toString();
            if(getten.trim().equals("")){
                Toast.makeText(context, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
            }
            else if(getten.equals(loaiSP.getTenloai())){
                Toast.makeText(context, "Tên cần phải khác tên cũ", Toast.LENGTH_SHORT).show();

            }
            else {
                loaiSPtmp.setTenloai(getten);
                LoaiSPEndpoint loaiSPEndpoint = new LoaiSPEndpoint();
                loaiSPEndpoint.editLSP(loaiSPtmp, new Callback<LoaiSP>() {
                    @Override
                    public void onResponse(Call<LoaiSP> call, Response<LoaiSP> response) {
                        if(response.isSuccessful()){
                            try {
                                list.set(posistion,response.body());
                                notifyDataSetChanged();
                                Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                            try {
                                Log.e(TAG, "onResponse: "+ response.errorBody().string());

                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoaiSP> call, Throwable t) {
                        Toast.makeText(context, "Kết nối vơi máy chủ thất bại", Toast.LENGTH_SHORT).show();
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
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenLoaiSP;
        TextView SL;
        Switch sw;
        Button sua;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenLoaiSP = itemView.findViewById(R.id.tvTen);
            SL = itemView.findViewById(R.id.tvSl);
            sw= itemView.findViewById(R.id.SWactivate);
            sua=itemView.findViewById(R.id.btnSua);
        }
    }
}