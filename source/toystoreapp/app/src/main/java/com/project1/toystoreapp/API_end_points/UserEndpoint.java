package com.project1.toystoreapp.API_end_points;

import com.project1.toystoreapp.API_services.UserService;
import com.project1.toystoreapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserEndpoint extends BaseAPIEndpoint{
    private UserService userService =createJsonForm(UserService.class);

    public UserEndpoint() {

    }
    public void validate(User user, Callback<User> callback){
        userService.ValidateUser(user).enqueue(callback);
    }
    public void createUser(User user, Callback<ResponseBody> callback){
        userService.addUser(user).enqueue(callback);
    }
}
