package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.LoaiSPCon;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoaiSPConService {
    @POST("loaispcon/create/{ParentID}")
    Call<LoaiSPCon> createLoaiSPCon(@Body LoaiSPCon loaiSPCon,@Path("ParentID") String ParentID);


    @GET("loaispcon/getallspconByParentID/{ParentID}")
    Call<List<LoaiSPCon>> getallspconByParentID(@Path("ParentID") String ParentID);

    @PUT("loaispcon/activeToggle")
    Call<LoaiSPCon> activeToggle(@Body LoaiSPCon loaiSPCon);

    @PUT("loaispcon/editlspcon")
    Call<LoaiSPCon> editLSPCon(@Body LoaiSPCon loaiSPCon);

    @DELETE("loaispcon/deleteLSPCon/{id}")
    Call<ResponseBody> deleteLSPCon(@Path("id") String id);
}
