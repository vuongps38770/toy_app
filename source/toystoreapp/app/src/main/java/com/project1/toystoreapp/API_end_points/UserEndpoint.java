package com.project1.toystoreapp.API_end_points;

import android.util.Log;

import com.project1.toystoreapp.API_services.UserService;
import com.project1.toystoreapp.Classes.PasswordHasher;
import com.project1.toystoreapp.model.Coderecive;
import com.project1.toystoreapp.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserEndpoint extends BaseAPIEndpoint{
    private UserService userService =createJsonForm(UserService.class);

    public UserEndpoint() {

    }
    public void validate(User user, Callback<User> callback){
        userService.ValidateUser(user).enqueue(callback);
    }
    public void createUser(User user, AddUserListener callback){
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        userService.addUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSucsess();
                }else {
                    callback.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onErrorr();
            }
        });
    }
    public void vaildteInfo(User user,VaildteInfolistener callback ){
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        userService.sendValidate(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    callback.onSucsess();
                }else {
                    if(response.code()==409){
                        callback.onFailure();
                        return;
                    }
                    callback.onErrorr();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onErrorr();
            }
        });
    }
    public void sendEmail(User user, SendEmaillistener callback){
        Map<String,Object> body = new HashMap<>();
        body.put("to",user.getEmail());
        body.put("subject","Mã xác nhận đăng ký tài khoản");
        body.put("text","đây là mã xác nhận của bạn vui lòng không chia sẻ cho bất kỳ ai");
        userService.sendmail(body).enqueue(new Callback<Coderecive>() {
            @Override
            public void onResponse(Call<Coderecive> call, Response<Coderecive> response) {
                if(response.isSuccessful()){
                    callback.onSucsess(response.body().getCode());
                }else {
                    callback.onFailure();
                    try {
                        Log.e("onFailure mail: ",response.errorBody().string() );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Coderecive> call, Throwable t) {
                callback.onErrorr();
            }
        });
    }
    public void findAccountByEmail(String email, FindUserByEmailListener callback){
        userService.findUserByEmail(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    callback.onSucsess(response.body());
                }else {
                    if(response.code()==404){
                        callback.onNotFound();
                    }else {
                        callback.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onErrorr();
            }
        });
    }
    public void changePW(User user, ChangePWListener callBack){
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        userService.updateUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    callBack.onSucsess();
                }else {
                    callBack.onFailure();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callBack.onErrorr();
            }
        });
    }

   public interface VaildteInfolistener{
        void onSucsess();
        void onFailure();
        void onErrorr();
   }
    public interface SendEmaillistener{
        void onSucsess(String code);
        void onFailure();
        void onErrorr();
    }
    public interface AddUserListener{
        void onSucsess();
        void onFailure();
        void onErrorr();
    }
    public interface FindUserByEmailListener{
        void onSucsess(User user);
        void onFailure();
        void onErrorr();
        void onNotFound();
    }
    public interface ChangePWListener{
        void onSucsess();
        void onFailure();
        void onErrorr();
    }
}
