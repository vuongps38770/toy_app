package com.project1.toystoreapp.API_end_points;

import android.util.Log;

import com.project1.toystoreapp.API_services.LoaiSPConService;
import com.project1.toystoreapp.model.LoaiSPCon;

import java.io.IOException;
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
    public void addSP(String LSPConID,String sanphamID, onADDSP callback){
        loaiSPConService.addSanPham(LSPConID,sanphamID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSuccess();
                }else {
                    callback.onFailure("Thêm thất bại, có lỗi xảy ra");
                    try {
                        Log.e("addSanPham", response.code()+response.errorBody().string() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Thêm thất bại, có lỗi xảy ra");
                Log.e( "addSanPham onFailure: ",t.getMessage() );
            }
        });
    }
    public void removeSP(String LSPConID,String sanphamID, onDeleteAwait callback){
        loaiSPConService.removeSanPham(LSPConID,sanphamID).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSuccess();
                }else {
                    callback.onFailure("Xoá thất bại, có lỗi xảy ra");
                    try {
                        Log.e("addSanPham", response.code()+response.errorBody().string() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Xoá thất bại, có lỗi xảy ra");
                Log.e( "addSanPham onFailure: ",t.getMessage() );
            }
        });
    }
    public interface onADDSP{
        void onSuccess();
        void onFailure(String error);
    }
    public interface onDeleteAwait{
        void onSuccess();
        void onFailure(String error);
    }
}
