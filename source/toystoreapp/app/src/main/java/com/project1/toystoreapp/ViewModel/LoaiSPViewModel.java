package com.project1.toystoreapp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoaiSPViewModel extends ViewModel {
    private MutableLiveData<Integer> soLuongLiveData = new MutableLiveData<>();

    public LiveData<Integer> getSoLuongLiveData() {
        return soLuongLiveData;
    }


}
