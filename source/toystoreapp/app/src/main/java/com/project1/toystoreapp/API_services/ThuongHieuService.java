package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.ThuongHieu;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ThuongHieuService {

    @POST("thuonghieu/create")
    Call<ResponseBody> createThuongHieu(@Body ThuongHieu thuongHieu);
    @POST("thuonghieu/checkKhongTonTai")
    Call<ResponseBody> checkKhongTonTai(@Query("tenthuonghieu") String tenthuonghieu);
    @GET("thuonghieu/getall")
    Call<List<ThuongHieu>> getAllThuongHieu();
    @PUT("thuonghieu/edit")
    Call<ThuongHieu> editThuongHieu(@Body ThuongHieu thuongHieu);

}
