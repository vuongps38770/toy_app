package com.project1.toystoreapp.API_end_points;

import android.util.Log;
import android.widget.Toast;

import com.project1.toystoreapp.API_services.SanPhamService;
import com.project1.toystoreapp.model.SanPham;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        body.put("urrlanh",sanPham.getUrrlanh());
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
                        } catch (IOException e) {
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

    public interface CreateSanPhamCallback{
        void onSuccess();
        void onTrungten();
        void onFailure(String message);
    }
}
