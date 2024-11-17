package com.project1.toystoreapp.RecyclerAdapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project1.toystoreapp.model.ThuongHieu;
import com.project1.toystoreapp.R;

import java.util.List;

public class Admin_QL_ThuongHieu_adapter extends RecyclerView.Adapter<Admin_QL_ThuongHieu_adapter.ViewHolder>{
    Context context;
    List<ThuongHieu> list;
    ItemOnclickListener listener;

    public Admin_QL_ThuongHieu_adapter(Context context, List<ThuongHieu> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(ItemOnclickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.thuong_hieu_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuongHieu thuongHieu = list.get(position);
        Glide.with(holder.anh)
                .load(thuongHieu.getUrlthumbnail())
                .placeholder(R.drawable.img_1)
                .error(com.denzcoskun.imageslider.R.drawable.default_error)
                .into(holder.anh);
        holder.tenHang.setText(thuongHieu.getTenthuonghieu());
//        holder.anh.setOnClickListener(v -> {
//            if (listener!=null){
//                listener.onItemClicked(thuongHieu,position);
//            }else {
//                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
//            }
//        });
        holder.clickItem(thuongHieu,position);
    }

    @Override
    public int getItemCount() {
        return (list!=null)?list.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tenHang;
        ImageView anh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenHang = itemView.findViewById(R.id.ten);
            anh = itemView.findViewById(R.id.anh);
        }
        public void clickItem(ThuongHieu thuongHieu,int posistion){
            itemView.setOnClickListener(v->{
                listener.onItemClicked(thuongHieu,posistion);
            });
            
        }

    }

    public interface ItemOnclickListener{
        void onItemClicked(ThuongHieu thuongHieu,int posistion);
    }
}
