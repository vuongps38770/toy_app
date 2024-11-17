package com.project1.toystoreapp.RecyclerAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project1.toystoreapp.API_end_points.LoaiSPConEndpoint;
import com.project1.toystoreapp.Classes.VerticalItemSpacingDecoration;
import com.project1.toystoreapp.Interfaces.OnItemOnclicked;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.LoaiSPCon;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Admin_QL_LSP_SPCON_outter_adapter extends RecyclerView.Adapter<Admin_QL_LSP_SPCON_outter_adapter.ViewHolder>{

    private List<LoaiSP> list;
    private Context context;
    private OnItemOnclicked onItemOnclicked;
    LoaiSPConEndpoint loaiSPConEndpoint = new LoaiSPConEndpoint();
    public Admin_QL_LSP_SPCON_outter_adapter(List<LoaiSP> list, Context context, OnItemOnclicked onItemOnclicked) {
        this.list = list;
        this.context = context;
        this.onItemOnclicked =onItemOnclicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView =LayoutInflater.from(context).inflate(R.layout.adminql_loai_sp_con_items,null,false);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(layoutParams);
        return new ViewHolder(rootView);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSP thisItem=list.get(position);
        if(thisItem==null)
            return;
        holder.title.setText(thisItem.getTenloai());
        resetItem(holder, thisItem, position);

    }

    private void resetItem(ViewHolder holder,LoaiSP thisItem, int position) {
        ((Activity)context).runOnUiThread(()->{
            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
            Admin_QL_LSP_LSPCON_adapter adapter= new Admin_QL_LSP_LSPCON_adapter(context,onItemOnclicked);
            adapter.setData(thisItem.getListLSPCON());
            holder.items.setHasFixedSize(true);
            holder.items.setLayoutManager(linearLayoutManager);
            holder.items.setAdapter(adapter);
            int space = 20;
            VerticalItemSpacingDecoration itemSpacingDecoration = new VerticalItemSpacingDecoration(space);
            holder.items.addItemDecoration(itemSpacingDecoration);
        });

        holder.add.setOnClickListener(v -> {
            createAddDialog(thisItem,position,holder);

        });
    }

    private void createAddDialog( LoaiSP parent,int position, ViewHolder holder) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_edit_tenlssp,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        EditText ten = v.findViewById(R.id.txtedtTensp);
        Button huy = v.findViewById(R.id.btnhuy);
        Button add = v.findViewById(R.id.btnsua);
        AlertDialog dialog = builder.create();
        dialog.show();
        huy.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        add.setOnClickListener(v1 -> {


            if(ten.getText().toString().trim().equals("")){
                MotionToast.Companion.createToast((Activity) context,
                        "Thiếu thông tin!",
                        "Vui lòng nhập đủ thông tin.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }

            loaiSPConEndpoint.createLoaiSPCon(new LoaiSPCon(parent.getId(), ten.getText().toString().trim(), 0), new Callback<>() {
                @Override
                public void onResponse(Call<LoaiSPCon> call, Response<LoaiSPCon> response) {
                    if (response.isSuccessful()) {
                        loaiSPConEndpoint.getallspconByParentID(parent.getId(), list1 -> {
                            if (!list1.isEmpty()) {
                                parent.setListLSPCON(list1);
                                list.set(position, parent);
                                notifyItemChanged(position);

                                resetItem(holder,parent,position);
                                Log.e("onResponse: ", list.get(position).getListLSPCON().size() + "");
                                dialog.dismiss();
                                MotionToast.Companion.createToast((Activity) context,
                                        "Thành công!",
                                        "Thêm thành công.",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                            }else {
                                Toast.makeText(context, "lỗi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        if (response.code() == 409) {
                            MotionToast.Companion.createToast((Activity) context,
                                    "Thông tin trùng lặp!",
                                    "Tên đã tồn tại.",
                                    MotionToastStyle.WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                        }else {
                            MotionToast.Companion.createToast((Activity) context,
                                    "Thất bại!",
                                    "Thêm thất bại, "+response.code(),
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                            dialog.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoaiSPCon> call, Throwable t) {
                    MotionToast.Companion.createToast((Activity) context,
                            "Thất bại!",
                            "Không thể kết nối với máy chủ.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return (list!=null)?list.size():0;
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button add;
        TextView title;
        RecyclerView items;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            add=itemView.findViewById(R.id.add);
            items=itemView.findViewById(R.id.recyclerview_lspcon);
        }
    }
}
