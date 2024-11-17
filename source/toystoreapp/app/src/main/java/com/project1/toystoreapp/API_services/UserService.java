package com.project1.toystoreapp.API_services;

import com.project1.toystoreapp.model.Coderecive;
import com.project1.toystoreapp.model.User;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface UserService {
    @POST("users/create")
    Call<ResponseBody> addUser(@Body User user);
    @POST("users/validate")
    Call<User> ValidateUser(@Body User user);
    @POST("send-email")
    Call<Coderecive> sendmail(@Body Map<String,Object> body);
    @POST("users/validetInf")
    Call<ResponseBody> sendValidate(@Body User body);
}