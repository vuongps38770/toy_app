package com.project1.toystoreapp.RecyclerAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project1.toystoreapp.Interfaces.OnItemOnclicked;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.model.LoaiSP;

import java.util.List;

public class Admin_QL_LSP_SPCON_outter_adapter extends RecyclerView.Adapter<Admin_QL_LSP_SPCON_outter_adapter.ViewHolder>{

    private List<LoaiSP> list;
    private Context context;
    private OnItemOnclicked onItemOnclicked;
    public Admin_QL_LSP_SPCON_outter_adapter(List<LoaiSP> list, Context context, OnItemOnclicked onItemOnclicked) {
        this.list = list;
        this.context = context;
        this.onItemOnclicked =onItemOnclicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adminql_loai_sp_con_items,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoaiSP thisItem=list.get(position);
        if(thisItem==null)
            return;
        holder.title.setText(thisItem.getTenloai());
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
        Admin_QL_LSP_LSPCON_adapter adapter= new Admin_QL_LSP_LSPCON_adapter(onItemOnclicked);
        adapter.setData(thisItem.getListLSPCON());
        holder.items.setLayoutManager(linearLayoutManager);
        holder.items.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        RecyclerView items;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            items=itemView.findViewById(R.id.recyclerview_lspcon);
        }
    }
}
