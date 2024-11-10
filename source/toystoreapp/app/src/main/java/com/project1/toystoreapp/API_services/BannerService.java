package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.Banner;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BannerService {

    @GET("banner/get/{id}")
    Call<Banner> getBanner(@Path("id") String id);

    @PUT("banner/edit/{id}")
    Call<Banner> setBanner(@Body Banner banner, @Path("id") String id);
}
