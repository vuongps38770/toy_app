package com.project1.toystoreapp.API_end_points;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.project1.toystoreapp.API_services.ThuongHieuService;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.ThuongHieu;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThuongHieuEndpoint extends BaseAPIEndpoint{
    ThuongHieuService thuongHieuService = createJsonForm(ThuongHieuService.class);
    public void createThuongHieu(String tenThuongHieu, Uri uri, Context context, CreateThuongHieuCallBack callBack){
        thuongHieuService.checkKhongTonTai(tenThuongHieu).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    CloudinaryUpload uploader = new CloudinaryUpload(context);
                    Toast.makeText(context, "Đang upload ảnh", Toast.LENGTH_SHORT).show();
                    upload(uploader,uri,tenThuongHieu,callBack);
                }else {
                    if(response.code()==409){
                        ///anhtrung
                        callBack.onTrungTen();
                    }else {
                        try {
                            Log.e("TAG", response.message()+response.code()+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ///loi
                        callBack.onLoi("Có lỗi xảy ra");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ///loi
                callBack.onLoi("Vui lòng kiểm tra kết nối");
            }
        });
    }

    public interface CreateThuongHieuCallBack{
        void onSuccess();
        void onLoi(String message);
        void onTrungTen();
        void onSave();
    }

    public void getAllThuongHieu( Consumer<List<ThuongHieu>> list){
        thuongHieuService.getAllThuongHieu().enqueue(new Callback<List<ThuongHieu>>() {
            @Override
            public void onResponse(Call<List<ThuongHieu>> call, Response<List<ThuongHieu>> response) {
                if(response.isSuccessful()){
                    list.accept(response.body());
                }else {
                    list.accept(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(Call<List<ThuongHieu>> call, Throwable t) {
                list.accept(Collections.emptyList());
            }
        });
    }

    private void upload(CloudinaryUpload uploader,Uri uri,String tenThuongHieu, CreateThuongHieuCallBack callBack){
        uploader.uploadImage(uri, new CloudinaryUpload.UploadCallback() {
            @Override
            public void onUploadSuccess(String imageUrl) {
                callBack.onSave();
                ThuongHieu thuongHieu = new ThuongHieu(tenThuongHieu,imageUrl.replaceFirst("http://","https://"));
                uploadToDB(thuongHieu,callBack);
            }
            @Override
            public void onUploadFailed() {
                ///them anh loi
                callBack.onLoi("Có lỗi xảy ra trong quá trình upload ảnh");
            }
        });
    }
    private void uploadToDB(ThuongHieu thuongHieu , CreateThuongHieuCallBack callBack){
        thuongHieuService.createThuongHieu(thuongHieu).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    ///ok
                    callBack.onSuccess();
                }else {
                    ///loi
                    callBack.onLoi("Có lỗi xảy ra api");
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ///loikn
                callBack.onLoi("Vui lòng kiểm tra kết nối");
            }
        });
    }
}
