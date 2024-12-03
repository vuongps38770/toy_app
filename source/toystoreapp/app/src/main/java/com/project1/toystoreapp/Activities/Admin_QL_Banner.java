package com.project1.toystoreapp.Activities;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.project1.toystoreapp.API_end_points.BannerEndpoint;
import com.project1.toystoreapp.Classes.CloudinaryUpload;
import com.project1.toystoreapp.Classes.ImagePicker;
import com.project1.toystoreapp.Classes.RequestPermission;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.databinding.ActivityAdminQlBannerBinding;
import com.project1.toystoreapp.model.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class Admin_QL_Banner extends AppCompatActivity {
    ActivityAdminQlBannerBinding binding;
    ImagePicker imagePicker;
    Banner banner;
    final Uri[] uris=new Uri[3];
    private int counter=0;
    final Banner[] banners = {null};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        banner = new Banner();
        imagePicker = new ImagePicker(this);
        binding = ActivityAdminQlBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Glide.with(binding.progress).load(R.drawable.loading).into(binding.progress);
        binding.back.setOnClickListener(v->{
            getOnBackPressedDispatcher().onBackPressed();
        });
        BannerEndpoint endpoint = new BannerEndpoint();
        endpoint.getBanner(new BannerEndpoint.OnGetBannerListener() {
            @Override
            public void onSuccess(Banner banner) {
                banners[0] =banner;
                ArrayList<SlideModel> imageList = new ArrayList<>();
                if(banners[0]==null){
                    imageList.add(new SlideModel(R.drawable.img_1,"",null));
                    imageList.add(new SlideModel(R.drawable.img_1,"",null));
                    imageList.add(new SlideModel(R.drawable.img_1,"",null));
                }else {
                    imageList.add(new SlideModel(banners[0].getUrl1(),"",null));
                    imageList.add(new SlideModel(banners[0].getUrl2(),"",null));
                    imageList.add(new SlideModel(banners[0].getUrl3(),"",null));
                    Log.e("save: 1",banners[0].getUrl1() );
                    Log.e("save: 2",banners[0].getUrl2() );
                    Log.e("save: 3",banners[0].getUrl3() );
                    binding.anh1.setOnClickListener(v -> {
                        uploadBanner(binding.anh1,0,uris);
                    });
                    binding.anh2.setOnClickListener(v -> {
                        uploadBanner(binding.anh2,1,uris);
                    });
                    binding.anh3.setOnClickListener(v -> {
                        uploadBanner(binding.anh3,2,uris);
                    });
                    binding.save.setOnClickListener(v -> {
                        save();
                    });
                }
                binding.imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP);
                binding.imageSlider.startSliding(2000);
            }

            @Override
            public void onFailure(String message) {
                MotionToast.Companion.createToast(Admin_QL_Banner.this,
                        "Thất bại!",
                        message,
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(Admin_QL_Banner.this, www.sanju.motiontoast.R.font.helvetica_regular));
                ArrayList<SlideModel> imageList = new ArrayList<>();
                imageList.add(new SlideModel(R.drawable.img_1,"",null));
                imageList.add(new SlideModel(R.drawable.img_1,"",null));
                imageList.add(new SlideModel(R.drawable.img_1,"",null));
                binding.imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP);
                binding.imageSlider.startSliding(2000);
            }
        });
    }
    public void uploadBanner(ImageView imageView,int imgIdx,Uri[] uris){
        if(RequestPermission.request(this, Manifest.permission.READ_EXTERNAL_STORAGE,1)){
            imagePicker.pickImage(new ImagePicker.ImagePickerCallback() {
                @Override
                public void onImagePicked(Uri uri) {
                    imageView.setImageURI(uri);
                    uris[imgIdx]=uri;
                    for(int i =0;i<uris.length;i++){
                        if(uris[i]!=null){
                            Log.e(i+"onCreate: ",uri.toString() );
                        }
                    }
                }
                @Override
                public void onPickCancelled() {
                    Toast.makeText(Admin_QL_Banner.this, "Đã huỷ", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(this, "Vui lòng cấp quyền", Toast.LENGTH_SHORT).show();
        }
    }

    private void save(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(uris[0]==null&&uris[1]==null&&uris[2]==null){
                    Toast.makeText(Admin_QL_Banner.this, "Vui lòng chọn", Toast.LENGTH_SHORT).show();
                    return;
                }
                counter=0;
                for (Uri uri : uris) {
                    if (uri != null) {
                        counter++;
                    }
                }

                binding.progress.setVisibility(View.VISIBLE);
                if(uris[0]!=null){
                    if(counter==-1){
                        Toast.makeText(Admin_QL_Banner.this, "Có lỗi xảy ra khi upload ảnh", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    upload(uris[0],0);
                }
                if(uris[1]!=null){
                    if(counter==-1){
                        Toast.makeText(Admin_QL_Banner.this, "Có lỗi xảy ra khi upload ảnh", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    upload(uris[1],1);
                }
                if(uris[2]!=null){
                    if(counter==-1){
                        Toast.makeText(Admin_QL_Banner.this, "Có lỗi xảy ra khi upload ảnh", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    upload(uris[2],2);
                }
            }
        });
    }

    private void saveToDB() {
        Log.e("save: 1",banners[0].getUrl1() );
        Log.e("save: 2",banners[0].getUrl2() );
        Log.e("save: 3",banners[0].getUrl3() );
        banners[0].setUrl1(banners[0].getUrl1().replaceFirst("http://", "https://"));
        banners[0].setUrl2(banners[0].getUrl2().replaceFirst("http://", "https://"));
        banners[0].setUrl3(banners[0].getUrl3().replaceFirst("http://", "https://"));

        BannerEndpoint bannerEndpoint = new BannerEndpoint();
        bannerEndpoint.setBanner(banners[0], new BannerEndpoint.OnGetBannerListener() {
            @Override
            public void onSuccess(Banner banner) {
                Toast.makeText(Admin_QL_Banner.this, "Đã lưu thành công", Toast.LENGTH_SHORT).show();
                binding.progress.setVisibility(View.GONE);
                recreate();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(Admin_QL_Banner.this, "Lưu lên server thất bại: "+message, Toast.LENGTH_SHORT).show();
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    private void upload(Uri uri,int idx) {
        CloudinaryUpload cloudinaryUpload = new CloudinaryUpload(Admin_QL_Banner.this);
        cloudinaryUpload.uploadImage(uri, new CloudinaryUpload.UploadCallback() {
            @Override
            public void onUploadSuccess(String imageUrl) {
                if(idx==0){
                    banners[0].setUrl1(imageUrl);
                    Log.e("save: 1",banners[0].getUrl1() );
                    Log.e("save: 2",banners[0].getUrl2() );
                    Log.e("save: 3",banners[0].getUrl3() );

                } else if (idx==1) {
                    banners[0].setUrl2(imageUrl);
                    Log.e("save: 1",banners[0].getUrl1() );
                    Log.e("save: 2",banners[0].getUrl2() );
                    Log.e("save: 3",banners[0].getUrl3() );
                }else {
                    banners[0].setUrl3(imageUrl);
                    Log.e("save: 1",banners[0].getUrl1() );
                    Log.e("save: 2",banners[0].getUrl2() );
                    Log.e("save: 3",banners[0].getUrl3() );
                }
                counter--;
                if(counter==0){
                    saveToDB();
                }

            }

            @Override
            public void onUploadFailed() {
                counter=-1;
            }
        });
    }

}