package com.project1.toystoreapp.RecyclerAdapters;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.project1.toystoreapp.API_end_points.LoaiSPConEndpoint;
import com.project1.toystoreapp.Classes.VerticalItemSpacingDecoration;
import com.project1.toystoreapp.Interfaces.OnItemOnclicked;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.LoaiSPCon;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Admin_QL_LSP_LSPCON_adapter extends RecyclerView.Adapter<Admin_QL_LSP_LSPCON_adapter.ViewHolder>{
    Context context;
    public Admin_QL_LSP_LSPCON_adapter(Context context,OnItemOnclicked onItemOnclicked) {
        this.onItemOnclicked=onItemOnclicked;
        this.context =context;
    }
    private OnItemOnclicked onItemOnclicked;
    List<LoaiSPCon> list;

    public void setData(List<LoaiSPCon> newList){
        this.list=newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView =LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_lspcon_layout_item,null,false);
        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(layoutParams);
        return new ViewHolder(rootView);
    }

    LoaiSPConEndpoint loaiSPConEndpoint = new LoaiSPConEndpoint();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSPCon loaiSPCon = list.get(position);
        holder.TenLSPcon.setText(loaiSPCon.getTenloai());
        holder.itemView.setOnClickListener(v->{
            onItemOnclicked.onClickedItem(loaiSPCon);
        });
        holder.isActive.setChecked(loaiSPCon.isActivated());
        holder.isActive.setOnClickListener(v -> {
            holder.isActive.setChecked((holder.isActive.isChecked())?false:true);
            createAtiveToggleDialog(loaiSPCon,position);

        });
        holder.btnSua.setOnClickListener(v -> {
            createEditDialog(loaiSPCon,position);
        });
        holder.btnXoa.setOnClickListener(v -> {
            createDeleteDialog(loaiSPCon,position);
        });

    }

    private void createAtiveToggleDialog(LoaiSPCon loaiSPCon, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(loaiSPCon.isActivated()?"Tắt danh mục":"Bật danh mục");
        builder.setMessage(loaiSPCon.isActivated()?"Bạn sẽ tắt danh muc trang chủ":"Bạn sẽ bật danh mục và hiện nó lên trang chủ");
        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            loaiSPConEndpoint.activeToggle(loaiSPCon, new Callback<>() {
                @Override
                public void onResponse(Call<LoaiSPCon> call, Response<LoaiSPCon> response) {
                    if(response.isSuccessful()){
                        list.set(position,response.body());
                        notifyDataSetChanged();
                        MotionToast.Companion.createToast((Activity) context,
                                "Thành công!",
                                "Đã cập nhật trạng thái.",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));

                    }else {
                        MotionToast.Companion.createToast((Activity) context,
                                "Thất bại!",
                                "Cập nhật thất bại, có lỗi đã xảy ra.",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                    }
                }

                @Override
                public void onFailure(Call<LoaiSPCon> call, Throwable t) {
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

    private void createEditDialog(LoaiSPCon loaiSPCon, int posistion) {
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_edit_tenlssp,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(v);
        EditText ten = v.findViewById(R.id.txtedtTensp);
        ten.setText(loaiSPCon.getTenloai());
        ten.setHint("Nhập tên danh mục");
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
            if(ten.getText().toString().trim().equals(loaiSPCon.getTenloai())){
                MotionToast.Companion.createToast((Activity) context,
                        "Thông tin chưa được chỉnh sửa!",
                        "Tên cân fphải khác tên cũ.",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                return;
            }
            LoaiSPCon clone = LoaiSPCon.clone(loaiSPCon);
            clone.setTenloai(ten.getText().toString().trim());
            loaiSPConEndpoint.editLSPCon(clone, new Callback<>() {
                @Override
                public void onResponse(Call<LoaiSPCon> call, Response<LoaiSPCon> response) {
                    if(response.isSuccessful()){
                        list.set(posistion,response.body());
                        notifyItemChanged(posistion);
                        MotionToast.Companion.createToast((Activity) context,
                                "Thành công!",
                                "Cập nhật thành công.",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                        dialog.dismiss();
                    }else {
                        if(response.code()==409){
                            MotionToast.Companion.createToast((Activity) context,
                                    "Trùng lặp!",
                                    "Tên này đã tồn tại.",
                                    MotionToastStyle.WARNING,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                        }else {
                            MotionToast.Companion.createToast((Activity) context,
                                    "Thất bại!",
                                    "Có lỗi đã xảy ra.",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.SHORT_DURATION,
                                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                            ///log
                            try {
                                Log.e("log editLSPCon",response.code()+ response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<LoaiSPCon> call, Throwable t) {
                    MotionToast.Companion.createToast((Activity) context,
                            "Thất bại!",
                            "Không thể kết nối với máy chủ.",
                            MotionToastStyle.WARNING,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                }
            });

        });
    }

    private void createDeleteDialog(LoaiSPCon loaiSPCon,int posistion){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String alert = "<font color='#FF0000'>Lưu ý, việc xoá sẽ loại bỏ danh mục này khỏi trang chủ</font>";
        String message ="Bạn sẽ xoá danh mục <b>"+loaiSPCon.getTenloai()+"</b> khỏi trang chủ<br>"+alert;
        builder.setMessage(Html.fromHtml(message,Html.FROM_HTML_MODE_LEGACY));
        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            loaiSPConEndpoint.deleteLSPCon(loaiSPCon, new LoaiSPConEndpoint.onDeleteAwait() {
                @Override
                public void onSuccess() {
                    list.remove(posistion);
                    notifyItemRemoved(posistion);
                    notifyItemRangeChanged(posistion, list.size());
                    MotionToast.Companion.createToast((Activity) context,
                            "Thành công!",
                            "Đã xoá thành công.",
                            MotionToastStyle.DELETE,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular));
                }

                @Override
                public void onFailure(String error) {
                    MotionToast.Companion.createToast((Activity) context,
                            "Thất bại!",
                            error,
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

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView TenLSPcon;
        ImageButton btnSua;
        ImageButton btnXoa;
        Switch isActive;
        TextView status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TenLSPcon= itemView.findViewById(R.id.tenloai);
            btnSua =itemView.findViewById(R.id.btnedit);
            btnXoa =itemView.findViewById(R.id.btndelete);
            isActive = itemView.findViewById(R.id.swIsActive);
            status = itemView.findViewById(R.id.status);

        }

    }

}
