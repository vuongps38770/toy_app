package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.SanPham;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SanPhamService {
    @POST("sanpham/create")
    Call<ResponseBody> createSanPham(@Body Map<String, Object> body);
    @GET("sanpham/getAllsanpham")
    Call<List<SanPham>> getAllSanPham();
    @PUT("sanpham/editSPByID/{id}")
    Call<SanPham> editSp(@Body Map<String,Object> body, @Path("id") String id);

    @GET("sanpham/getAllsanphamByLoaiSPconID/{id}")
    Call<List<SanPham>> getAllsanphamByLoaiSPconID(@Path("id") String spconID);

    @GET("sanpham/getAllsanphamNotHaveLoaiSPconID/{id}")
    Call<List<SanPham>> getAllsanphamNotHaveLoaiSPconID(@Path("id") String spconID);
}
