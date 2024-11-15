package com.project1.toystoreapp.RecyclerAdapters;
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
import androidx.recyclerview.widget.RecyclerView;
import com.project1.toystoreapp.API_end_points.LoaiSPConEndpoint;
import com.project1.toystoreapp.Interfaces.OnItemOnclicked;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.LoaiSPCon;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        builder.setTitle("Xoá danh mục");
        builder.setMessage("Bạn sẽ xoá danh muc trang chủ");
        builder.setPositiveButton("Xác nhận", ((dialog, which) -> {
            loaiSPConEndpoint.activeToggle(loaiSPCon, new Callback<>() {
                @Override
                public void onResponse(Call<LoaiSPCon> call, Response<LoaiSPCon> response) {
                    if(response.isSuccessful()){
                        list.set(position,response.body());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoaiSPCon> call, Throwable t) {
                    Toast.makeText(context, "Sửa thất bại, kết nối với máy chủ thất bại", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if(ten.getText().toString().trim().equals(loaiSPCon.getTenloai())){
                Toast.makeText(context, "Tên cần phải khác tên cũ", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        if(response.code()==409){
                            Toast.makeText(context, "Tên này đã tòn tại", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Cập nhật thất bại, có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Cập nhật thất bại, không thể kết nối với máy chủ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Đã xoá thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TenLSPcon= itemView.findViewById(R.id.tenloai);
            btnSua =itemView.findViewById(R.id.btnedit);
            btnXoa =itemView.findViewById(R.id.btndelete);
            isActive = itemView.findViewById(R.id.swIsActive);

        }

    }

}
