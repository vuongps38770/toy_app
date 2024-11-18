package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.LoaiSP_user;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoaiSPService {

    @POST("loaisp/create")
    Call<LoaiSP> createLoaiSP(@Body LoaiSP loaiSP);

    @GET("loaisp/getAllLoaiSP")
    Call<List<LoaiSP>> getAllLoaiSP();

    @GET("loaispcon/getSoLuongByParentID/{ParentID}")
    Call<Integer> getSoLuongByID(@Path("ParentID" )String ParentID);

    @PUT("loaisp/editLoaiSP")
    Call<LoaiSP> suaLoaiSanPham(@Body LoaiSP loaiSP);

    @POST("loaisp/create")
    Call<ResponseBody> addLoaiSanPham(@Body LoaiSP loaiSP);

    @PUT("loaisp/activateToggle/{id}")
    Call<LoaiSP> activateToggle(@Body LoaiSP loaiSP,@Path("id") String id);

    @GET("loaisp/getAllLoaiSPPopulate")
    Call<List<LoaiSP>> GetAllLoaiSPPopulate();

    @GET("loaisp/getAllLoaiSPPopulate")
    Call<List<LoaiSP_user>> GetAllLoaiSPPopulateforUser();

    @DELETE("loaisp/deleteLoaiSP/{id}")
    Call<ResponseBody> DeleteLoaiSP(@Path("id") String id);

    
}
