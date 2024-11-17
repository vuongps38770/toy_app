package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.Coderecive;
import com.project1.toystoreapp.model.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @POST("users/create")
    Call<ResponseBody> addUser(@Body User user);
    @POST("users/validate")
    Call<User> ValidateUser(@Body User user);
    @POST("send-email")
    Call<Coderecive> sendmail(@Body Map<String,Object> body);
    @POST("users/validetInf")
    Call<ResponseBody> sendValidate(@Body User body);
    @POST("users/findAccountByEmail/{email}")
    Call<User> findUserByEmail(@Path("email") String email);

    @POST("users/updateUser")
    Call<User> updateUser(@Body User user);
}