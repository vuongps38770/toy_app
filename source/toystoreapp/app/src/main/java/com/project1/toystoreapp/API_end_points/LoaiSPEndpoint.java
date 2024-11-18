package com.project1.toystoreapp.API_end_points;

import android.util.Log;

import com.google.gson.Gson;
import com.project1.toystoreapp.API_services.LoaiSPService;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.LoaiSP_user;
import com.project1.toystoreapp.model.SanPham;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoaiSPEndpoint extends BaseAPIEndpoint{
    private LoaiSPService loaiSPService=createJsonForm(LoaiSPService.class);
    public LoaiSPEndpoint() {
    }

    public void getAllLoaiSP(Consumer<List<LoaiSP>> list){
        loaiSPService.getAllLoaiSP().enqueue(new Callback<List<LoaiSP>>() {
            @Override
            public void onResponse(Call<List<LoaiSP>> call, Response<List<LoaiSP>> response) {
                if (response.isSuccessful()&& response.body()!=null&&response!=null){
                    Log.e("fghjklfdsdfgh",String.valueOf(response.body().size()));
                    list.accept(response.body());
                }else {
                    list.accept(Collections.emptyList());
                    Log.e("vbnm,bnm",response.message());
                }
            }
            @Override
            public void onFailure(Call<List<LoaiSP>> call, Throwable t) {
                list.accept(Collections.emptyList());
                Log.e("cfvgbhnjmk,",t.getMessage());
            }
        });
    }
    public void getSoLuongByID(String parentID,Callback<Integer> callback){
        loaiSPService.getSoLuongByID(parentID).enqueue(callback);
    }
    public void editLSP (LoaiSP loaiSP, Callback<LoaiSP> callback){
        loaiSPService.suaLoaiSanPham(loaiSP).enqueue(callback);
    }

    public void activateToggle(LoaiSP loaiSP,Callback<LoaiSP> callback){
        loaiSPService.activateToggle(loaiSP,loaiSP.getId()).enqueue(callback);
    }
    public void addLoaiSanPham(LoaiSP loaiSP, Callback<ResponseBody> callback){
        loaiSPService.addLoaiSanPham(loaiSP).enqueue(callback);
    }

    public void getAllLoaiSPPopulate(Consumer<List<LoaiSP>> listLSP){
        loaiSPService.GetAllLoaiSPPopulate().enqueue(new Callback<List<LoaiSP>>() {
            @Override
            public void onResponse(Call<List<LoaiSP>> call, Response<List<LoaiSP>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    listLSP.accept(response.body());

                }else {
                    listLSP.accept(Collections.emptyList());
                    try {
                        Log.e("onResponse: ", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<LoaiSP>> call, Throwable t) {
                listLSP.accept(Collections.emptyList());
                Log.e("onResponse: ", t.getMessage());
            }
        });
    }

    public void getAllLoaiSPPopulateForUser(Consumer<List<LoaiSP_user>> listLSP){
        loaiSPService.GetAllLoaiSPPopulateforUser().enqueue(new Callback<List<LoaiSP_user>>() {
            @Override
            public void onResponse(Call<List<LoaiSP_user>> call, Response<List<LoaiSP_user>> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    listLSP.accept(response.body());
                    Gson gson = new Gson();
                    Log.e("onResponse: ", gson.toJson(response.body()));

                }else {
                    listLSP.accept(Collections.emptyList());
                    try {
                        Log.e("onResponse: ", response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<LoaiSP_user>> call, Throwable t) {
                listLSP.accept(Collections.emptyList());
                Log.e("onResponse: ", t.getMessage());
            }
        });
    }
    public void deleteLoaiSP(LoaiSP loaiSP, onDeleteAwait callBack){
        loaiSPService.DeleteLoaiSP(loaiSP.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess();
                }else {
                    callBack.onFailure("Xoá thất bại, có ỗi xảy ra");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onFailure("Xoá thất bại, không thể kết nối đên máy chủ");
            }
        });
    }

    public interface onDeleteAwait{
        void onSuccess();
        void onFailure(String errorr);
    }
}
