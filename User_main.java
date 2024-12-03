package com.project1.toystoreapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project1.toystoreapp.API_end_points.BannerEndpoint;
import com.project1.toystoreapp.API_end_points.LoaiSPEndpoint;
import com.project1.toystoreapp.API_end_points.ThuongHieuEndpoint;
import com.project1.toystoreapp.API_end_points.UserEndpoint;
import com.project1.toystoreapp.Enum.IntentKeys;
import com.project1.toystoreapp.Enum.PaymentStatus;
import com.project1.toystoreapp.RecyclerAdapters.SearchListAdapter;
import com.project1.toystoreapp.RecyclerAdapters.UserSanPhamInnerAdapter;
import com.project1.toystoreapp.RecyclerAdapters.UserSanPhamOutterAdapter;
import com.project1.toystoreapp.RecyclerAdapters.ViewPagerAdapter;
import com.project1.toystoreapp.R;
import com.project1.toystoreapp.RecyclerAdapters.User_getThuongHieu_Adapter;
import com.project1.toystoreapp.databinding.ActivityUserMainBinding;
import com.project1.toystoreapp.model.Banner;
import com.project1.toystoreapp.model.LoaiSP;
import com.project1.toystoreapp.model.LoaiSPCon_user;
import com.project1.toystoreapp.model.LoaiSP_user;
import com.project1.toystoreapp.model.SanPham_user;
import com.project1.toystoreapp.model.ThuongHieu;
import com.project1.toystoreapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class User_main extends AppCompatActivity implements  UserSanPhamOutterAdapter.UserItemClickListener, UserSanPhamOutterAdapter.SeemorrOnClickListener,User_getThuongHieu_Adapter.ItemOnclickListener{
    ActivityUserMainBinding binding;
    Banner banner;
    Banner[] banners={null};
    BannerEndpoint bannerEndpoint;
    ThuongHieuEndpoint thuongHieuEndpoint;
    LoaiSPEndpoint loaiSPEndpoint;
    List<SanPham_user> listAllsp= new ArrayList<>();
    User user;
    UserEndpoint userEndpoint;
    String paymentStatus ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.search.setIconified(true);
        user = new User();
        Intent i = getIntent();
        user = (User) i.getSerializableExtra(IntentKeys.ACCOUNT.name());
        if (user==null){
            Toast.makeText(this, "lỗi", Toast.LENGTH_SHORT).show();
            return;
        }
        paymentStatus=i.getStringExtra(IntentKeys.PAYMENT_SUCCESS.name());
        if (paymentStatus!=null&& paymentStatus.equals(PaymentStatus.SUCCESS_ORDERED_BY_CASH.name())){
            binding.overlay.setBackground(new ColorDrawable(Color.parseColor("#80FFFFFF")));
            binding.overlay.setVisibility(View.VISIBLE);
            binding.imgoverlay.setVisibility(View.VISIBLE);
            Glide.with(binding.overlay).load(R.drawable.success)
                    .placeholder(R.drawable.success)
                    .into(binding.imgoverlay);

            new Handler().postDelayed(() -> {
                binding.overlay.setVisibility(View.GONE);
                binding.imgoverlay.setVisibility(View.GONE);
            },2000);
        }
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()){
                    binding.searchview.setVisibility(View.GONE);
                    return true;
                }
                binding.searchview.setVisibility(View.VISIBLE);
                SearchListAdapter adapter = new SearchListAdapter(User_main.this, listAllsp, new SearchListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(SanPham_user sanPham) {
                        Intent intent = new Intent(User_main.this, CTSP.class);
                        intent.putExtra(IntentKeys.PRODUCT.name(), sanPham);
                        intent.putExtra(IntentKeys.ACCOUNT.name(), user);
                        startActivity(intent);
                    }
                });
                adapter.search(query);
                binding.searchList.setLayoutManager(new LinearLayoutManager(User_main.this,LinearLayoutManager.VERTICAL,false));
                binding.searchList.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    binding.searchview.setVisibility(View.GONE);
                    return true;
                }
                binding.searchview.setVisibility(View.VISIBLE);
                SearchListAdapter adapter = new SearchListAdapter(User_main.this, listAllsp, new SearchListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(SanPham_user sanPham) {
                        Intent intent = new Intent(User_main.this, CTSP.class);
                        intent.putExtra(IntentKeys.PRODUCT.name(), sanPham);
                        intent.putExtra(IntentKeys.ACCOUNT.name(), user);
                        startActivity(intent);
                    }
                });
                adapter.search(newText);
                binding.searchList.setLayoutManager(new LinearLayoutManager(User_main.this,LinearLayoutManager.VERTICAL,false));
                binding.searchList.setAdapter(adapter);
                return false;
            }
        });
        banner= new Banner();
        bannerEndpoint= new BannerEndpoint();
        thuongHieuEndpoint = new ThuongHieuEndpoint();
        loaiSPEndpoint= new LoaiSPEndpoint();
        setupBanner();
        setupBrandView();
        setupTabNViewpagers();
        View header = binding.navigation.getHeaderView(0);
        TextView username = header.findViewById(R.id.name);
        if(username!=null&&user.getUsername()!=null){
            String[] fullname=user.getUsername().split(" ");
            username.setText(fullname[fullname.length-1]);
        }

        binding.navigation.setNavigationItemSelectedListener(item -> {
            if(item.getItemId()== R.id.logout){
                SharedPreferences sharedPreferences = getSharedPreferences(IntentKeys.ACCOUNT.name(), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(User_main.this,Login.class);
                startActivity(intent);
                finish();
            }else if(item.getItemId()== R.id.changePW){
                Intent intent = new Intent(User_main.this,ForgotPW.class);
                startActivity(intent);
            } else if (item.getItemId() ==R.id.bill) {
                Intent intent = new Intent(User_main.this,DonHang_status.class);
                intent.putExtra(IntentKeys.ACCOUNT.name(), user);
                startActivity(intent);
            }
            return true;
        });
        binding.bottomAppBar.setOnItemSelectedListener(item -> {
            Intent intent;
            if(item.getItemId()==R.id.cart){
                intent = new Intent(User_main.this,Cart.class);
                intent.putExtra(IntentKeys.ACCOUNT.name(), user);
                startActivity(intent);
            } else if (item.getItemId()==R.id.account) {
                if (binding.main.isDrawerOpen(GravityCompat.END)) {
                    binding.main.closeDrawer(GravityCompat.END);
                } else {
                    binding.main.openDrawer(GravityCompat.END);
                }
            }
            return false;
        });

    }


    private void setupTabNViewpagers() {
        runOnUiThread(()->{
            loaiSPEndpoint.getAllLoaiSPPopulateForUser(list -> {
                listAllsp = list.stream()
                        .flatMap(loaiSP -> loaiSP.getListLSPCON().stream())
                        .flatMap(loaiSPCon -> loaiSPCon.getListsanphamID().stream())
                        .filter(sanPham -> sanPham.getIsActivate() == 1)
                        .collect(Collectors.toMap(
                                SanPham_user::getId,
                                sanPham -> sanPham,
                                (existing, replacement) -> existing
                        ))
                        .values()
                        .stream()
                        .collect(Collectors.toList());
                for (LoaiSP_user loaiSP : list) {
                    Log.d("ListInfo", "Tên loại sản phẩm: " + loaiSP.getTenloai());
                }

                list.removeIf(loaiSPUser ->
                    loaiSPUser.getIsActivate()==0||loaiSPUser.getListLSPCON().isEmpty()
                );


                ViewPagerAdapter adapter = new ViewPagerAdapter(this,list,this);
                adapter.setListener(this);
                adapter.setSeemoreListener(this);
                binding.pager.setAdapter(adapter);
                new TabLayoutMediator(binding.tabsp,binding.pager,(tab, position) -> {
                    tab.setText(list.get(position).getTenloai());
                }).attach();
            });
        });
    }

    private void setupBrandView() {
        thuongHieuEndpoint.getAllThuongHieuActivated(list->{
            if(list.size()>0){
                User_getThuongHieu_Adapter adapter = new User_getThuongHieu_Adapter(this,list);
                adapter.setListener(this);
                binding.thuonghieuRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
                binding.thuonghieuRecyclerView.setAdapter(adapter);
            }
        });
    }

    private void setupBanner() {
        bannerEndpoint.getBanner(new BannerEndpoint.OnGetBannerListener() {
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
                }
                binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
                binding.imageSlider.startSliding(2000);
            }

            @Override
            public void onFailure(String message) {
                MotionToast.Companion.createToast(User_main.this,
                        "Thất bại!",
                        message,
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(User_main.this, www.sanju.motiontoast.R.font.helvetica_regular));
                ArrayList<SlideModel> imageList = new ArrayList<>();
                imageList.add(new SlideModel(R.drawable.img_1,"",null));
                imageList.add(new SlideModel(R.drawable.img_1,"",null));
                imageList.add(new SlideModel(R.drawable.img_1,"",null));
                binding.imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP);
                binding.imageSlider.startSliding(2000);
            }
        });
    }


    @Override
    public void onSanPhamCicked(SanPham_user sanPham) {

        Intent intent = new Intent(this, CTSP.class);
        intent.putExtra(IntentKeys.PRODUCT.name(), sanPham);
        intent.putExtra(IntentKeys.ACCOUNT.name(), user);
        startActivity(intent);
    }

    @Override
    public void onSeemoreCicked(LoaiSPCon_user loaiSPCon) {
        ////trang xem thêm
        Intent intent = new Intent(User_main.this, SeeAll.class);
        intent.putExtra(IntentKeys.ACCOUNT.name(), user);
        intent.putExtra(IntentKeys.DANHMUC.name(), loaiSPCon);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(ThuongHieu thuongHieu, int posistion) {
        Intent intent = new Intent(User_main.this, SeeAll.class);
        intent.putExtra(IntentKeys.ACCOUNT.name(), user);
        intent.putExtra(IntentKeys.HANG.name(), thuongHieu);
        startActivity(intent);
    }
}