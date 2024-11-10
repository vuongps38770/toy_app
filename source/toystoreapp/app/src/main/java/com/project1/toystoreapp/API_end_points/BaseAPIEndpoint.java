package com.project1.toystoreapp.API_end_points;

import android.util.Log;

import java.util.function.Consumer;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseAPIEndpoint {
    private String dbname = "toystoreDB/";
    protected Retrofit retrofit;

    public BaseAPIEndpoint() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.4:3000/" + dbname)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }
    protected <T> T createJsonForm(Class<T> service){
        return retrofit.create(service);
    }
}
