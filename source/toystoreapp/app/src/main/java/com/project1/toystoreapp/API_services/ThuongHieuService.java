package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.Classes.ThuongHieu;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ThuongHieuService {

    @POST("thuonghieu/create")
    Call<ResponseBody> createThuongHieu(@Body ThuongHieu thuongHieu);
    @POST("thuonghieu/checkKhongTonTai")
    Call<ResponseBody> checkKhongTonTai(@Query("tenthuonghieu") String tenthuonghieu);
    @GET("thuonghieu/getall")
    Call<List<ThuongHieu>> getAllThuongHieu();


}
