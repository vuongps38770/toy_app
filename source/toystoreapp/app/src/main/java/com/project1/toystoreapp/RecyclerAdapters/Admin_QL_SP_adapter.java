package com.project1.toystoreapp.RecyclerAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class Admin_QL_SP_adapter extends RecyclerView.Adapter<Admin_QL_SP_adapter.ViewHolder> {
    Context context;
    List<SanPham> list;
    List<SanPham> oglist;
    public Admin_QL_SP_adapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
        this.oglist=list;
    }
    private ItemClicklistener itemClicklistener;

    public ItemClicklistener getItemClicklistener() {
        return itemClicklistener;
    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_qly_sp_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SanPham thisSP= list.get(position);
        Glide.with(holder.anh)
                .load(thisSP.getUrlanh())
                .error(R.drawable.img_1)
                .into(holder.anh);
        holder.tensp.setText(thisSP.getTensanpham());
        holder.gia.setText("Giá: "+thisSP.getGia());
        Log.e("onBindViewHolder: ", thisSP.getUrlanh());
        holder.setItemClicked(thisSP,position);
    }

    @Override
    public int getItemCount() {
        if(!list.isEmpty()){
            return list.size();
        }
        return 0;
    }

    public void search(String content) {
        if(content.equals("")){
            list = oglist;
            notifyDataSetChanged();
        }else {
            List<SanPham> res= new ArrayList<>();
            for(SanPham item:oglist){
                if(item.getTensanpham().toLowerCase().contains(content)){
                    res.add(item);
                }
            }
            list= res;
            notifyDataSetChanged();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tensp;
        ShapeableImageView anh;
        TextView gia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.txtTensp);
            anh = itemView.findViewById(R.id.img);
            gia = itemView.findViewById(R.id.gia);
        }
        public void setItemClicked(SanPham sanPham,int posistion){
            itemView.setOnClickListener(v -> {
                itemClicklistener.onItemClicked(sanPham,posistion);
            });
        }
    }
    public  interface ItemClicklistener{
        void onItemClicked(SanPham sanPham,int posistion);
    }
}
