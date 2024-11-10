package com.project1.toystoreapp.API_end_points;

import com.project1.toystoreapp.API_services.BannerService;
import com.project1.toystoreapp.model.Banner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BannerEndpoint extends BaseAPIEndpoint{
    private final String IDV1 ="6730239e704deb1d181f6cdc";
    public BannerEndpoint() {
    }

    private BannerService bannerService=  createJsonForm(BannerService.class);

    public void getBanner(OnGetBannerListener callback){
        bannerService.getBanner(IDV1).enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(Call<Banner> call, Response<Banner> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    callback.onFailure("có lỗi xảy ra");
                }
            }

            @Override
            public void onFailure(Call<Banner> call, Throwable t) {
                callback.onFailure("Vui lòng kiểm tra kết nối");

            }
        });
    }
    public void setBanner(Banner banner,OnGetBannerListener callback){
        bannerService.setBanner(banner,IDV1).enqueue(new Callback<Banner>() {
            @Override
            public void onResponse(Call<Banner> call, Response<Banner> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    callback.onFailure("lỗi");
                }
            }

            @Override
            public void onFailure(Call<Banner> call, Throwable t) {
                callback.onFailure("Lỗi kết nối");

            }
        });
    }

    public interface OnGetBannerListener{
        void onSuccess(Banner banner);
        void onFailure(String message);
    }

}
