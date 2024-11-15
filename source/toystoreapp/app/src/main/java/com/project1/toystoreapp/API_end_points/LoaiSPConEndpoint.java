package com.project1.toystoreapp.API_end_points;

import android.util.Log;

import com.project1.toystoreapp.API_services.LoaiSPConService;
import com.project1.toystoreapp.model.LoaiSPCon;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoaiSPConEndpoint extends BaseAPIEndpoint{

    LoaiSPConService loaiSPConService = createJsonForm(LoaiSPConService.class);

    public void createLoaiSPCon(LoaiSPCon loaiSPCon, Callback<LoaiSPCon> callback){
        loaiSPConService.createLoaiSPCon(loaiSPCon,loaiSPCon.getParentID()).enqueue(callback);
    }

    public void getallspconByParentID(String ParentID, Consumer<List<LoaiSPCon>> list){
        loaiSPConService.getallspconByParentID(ParentID).enqueue(new Callback<List<LoaiSPCon>>() {
            @Override
            public void onResponse(Call<List<LoaiSPCon>> call, Response<List<LoaiSPCon>> response) {
                if(response.isSuccessful()){
                    list.accept(response.body());
                }else {
                    list.accept(Collections.emptyList());
                    Log.e( "onResponse: ", "list rỗng");
                }
            }

            @Override
            public void onFailure(Call<List<LoaiSPCon>> call, Throwable t) {
                list.accept(Collections.emptyList());
                Log.e( "onResponse: ", " lỗi respond"+t.getMessage());
            }
        });
    }
    public  void activeToggle(LoaiSPCon loaiSPCon, Callback<LoaiSPCon> callback){
        loaiSPConService.activeToggle(loaiSPCon).enqueue(callback);
    }
    public  void editLSPCon(LoaiSPCon loaiSPCon, Callback<LoaiSPCon> callback){
        loaiSPConService.editLSPCon(loaiSPCon).enqueue(callback);
    }
    public void deleteLSPCon(LoaiSPCon loaiSPCon, onDeleteAwait callback){
        loaiSPConService.deleteLSPCon(loaiSPCon.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSuccess();
                }else {
                    callback.onFailure("Xoá thất bại, có lỗi xảy ra");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Không thể kết nối máy chủ");
            }
        });
    }

    public interface onDeleteAwait{
        void onSuccess();
        void onFailure(String error);
    }
}
