package com.project1.toystoreapp.RecyclerAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project1.toystoreapp.Interfaces.OnItemOnclicked;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.LoaiSPCon;

import java.util.List;

public class Admin_QL_LSP_LSPCON_adapter extends RecyclerView.Adapter<Admin_QL_LSP_LSPCON_adapter.ViewHolder>{
    public Admin_QL_LSP_LSPCON_adapter(OnItemOnclicked onItemOnclicked) {
        this.onItemOnclicked=onItemOnclicked;
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
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_lspcon_layout_item,null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSPCon loaiSPCon = list.get(position);
        holder.TenLSPcon.setText(loaiSPCon.getTenloai());
        holder.itemView.setOnClickListener(v->{
            onItemOnclicked.onClickedItem(loaiSPCon);
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView TenLSPcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TenLSPcon= itemView.findViewById(R.id.tenloai);
        }
    }
}
