package com.project1.toystoreapp.API_end_points;

import android.util.Log;

import com.project1.toystoreapp.API_services.SanPhamService;
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

public class SanPhamEndpoint extends BaseAPIEndpoint{
    SanPhamService sanPhamService = createJsonForm(SanPhamService.class);
    public void addSanPham(SanPham sanPham, CreateSanPhamCallback callback) {
        Map<String, Object> body = new HashMap<>();
        body.put("tensanpham",sanPham.getTensanpham());
        body.put("gia",sanPham.getGia());
        body.put("mota",sanPham.getMota());
        body.put("urlanh",sanPham.getUrlanh().replaceFirst("http://","https://"));
        body.put("thuonghieu",sanPham.getThuonghieu().getId());
        Log.e("addSanPham: ",sanPham.getThuonghieu().getId());
        body.put("isActivate",sanPham.getIsActivate());
        body.put("isInMainScreen",sanPham.getIsInMainScreen());

        sanPhamService.createSanPham(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSuccess();
                }else {
                    if(response.code()==409){
                        callback.onTrungten();
                    }else {
                        callback.onFailure("Thêm thất bại");
                        try {
                            Log.e("onResponse: ",response.body().string() );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure("Lỗi kết nói máy chủ");
            }
        });
    }
    public void editSP(SanPham sanPham,EditSanPhamCallback callback){
        Map<String,Object> body = new HashMap<>();
        body.put("tensanpham",sanPham.getTensanpham());
        body.put("gia",sanPham.getGia());
        body.put("mota",sanPham.getMota());
        body.put("urlanh",sanPham.getUrlanh().replaceFirst("http://","https://"));
        body.put("isActivate",sanPham.getIsActivate());
        body.put("isInMainScreen",sanPham.getIsInMainScreen());
        sanPhamService.editSp(body,sanPham.getId()).enqueue(new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }else {
                    callback.onFailure("Thêm sản phẩm thất bại, có lỗi xảy ra");
                }

            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {
                callback.onFailure("Lỗi xảy ra khi kết nối với máy chủ");
                Log.e("onFailure: ",t.getMessage() );
            }
        });
    }

    public void getAllsanphamNotHaveLoaiSPconID(String SPConID,Consumer<List<SanPham>> list){
        sanPhamService.getAllsanphamNotHaveLoaiSPconID(SPConID).enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if(response.isSuccessful()){
                    list.accept(response.body());
                    Log.e("onResponse: ",response.code()+""+response.body() );
                }else {
                    list.accept(Collections.emptyList());
                    try {
                        Log.e("getAllsanphamNotHaveLoaiSPconID:NOT IN 2xx range",response.code()+response.errorBody().string() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                list.accept(Collections.emptyList());
                Log.e("getAllsanphamNotHaveLoaiSPconID onFailure: ", t.getMessage());
            }
        });
    }
    public void getAllsanphamByLoaiSPconID(String SPConID,Consumer<List<SanPham>> list){
        sanPhamService.getAllsanphamByLoaiSPconID(SPConID).enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if(response.isSuccessful()){
                    list.accept(response.body());
                    Log.e("onResponse: ",response.code()+""+response.raw() );
                }else {
                    list.accept(Collections.emptyList());
                    try {
                        Log.e("getAllsanphamByLoaiSPconID:NOT IN 2xx range",response.code()+response.errorBody().string() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                list.accept(Collections.emptyList());
                Log.e("getAllsanphamByLoaiSPconID onFailure: ", t.getMessage());
            }
        });
    }
    public interface CreateSanPhamCallback{
        void onSuccess();
        void onTrungten();
        void onFailure(String message);
    }
    public interface EditSanPhamCallback{
        void onSuccess(SanPham sanPham);
        void onFailure(String message);
    }

    public void getAllSP(Consumer<List<SanPham>> list){
        sanPhamService.getAllSanPham().enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if(response.isSuccessful()){
                    list.accept(response.body());
                }else {
                    list.accept(Collections.emptyList());
                    try {
                        Log.e( "onResponse: ", response.code()+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SanPham>> call, Throwable t) {
                list.accept(Collections.emptyList());
                Log.e( "onResponse: ", t.getMessage());
            }
        });
    }
}
