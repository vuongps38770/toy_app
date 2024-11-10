package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface UserService {
    @POST("users")
    Call<ResponseBody> addUser(@Body User user);
    @POST("users/validate")
    Call<User> ValidateUser(@Body User user);
}