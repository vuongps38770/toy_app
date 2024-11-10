package com.project1.toystoreapp.API_services;

import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SanPhamService {
    @POST("sanpham/create")
    Call<ResponseBody> createSanPham(@Body Map<String, Object> body);

}
