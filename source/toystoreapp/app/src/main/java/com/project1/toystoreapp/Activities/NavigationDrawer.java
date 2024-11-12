package com.project1.toystoreapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;
import com.project1.toystoreapp.Fragment.DoanhThuFragment;
import com.project1.toystoreapp.Fragment.DoiMatKhauFragment;
import com.project1.toystoreapp.Fragment.GioHangFragment;
import com.project1.toystoreapp.Fragment.HomeFragment;
import com.project1.toystoreapp.Fragment.QLSPFragment;
import com.project1.toystoreapp.Fragment.Top10Fragment;
import com.project1.toystoreapp.Fragment.XacThucFragment;
import com.project1.toystoreapp.R;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        // Ánh xạ DrawerLayout và NavigationView
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Thiết lập Toolbar và ActionBarDrawerToggle
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Nếu savedInstanceState là null, chọn fragment mặc định là HomeFragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // Hiển thị HomeFragment ban đầu
            navigationView.setCheckedItem(R.id.nav_home); // Đánh dấu mục "Trang Chủ" là được chọn
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        // Dùng if-else để chọn Fragment dựa trên itemId
        if (item.getItemId() == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (item.getItemId() == R.id.nav_giohang) {
            selectedFragment = new GioHangFragment();
        } else if (item.getItemId() == R.id.nav_quanlysp) {
            selectedFragment = new QLSPFragment();
        } else if (item.getItemId() == R.id.nav_xacthuc) {
            selectedFragment = new XacThucFragment();
        }else if (item.getItemId() == R.id.nav_top10) {
            selectedFragment = new Top10Fragment();
        } else if (item.getItemId() == R.id.nav_doanhthu) {
            selectedFragment = new DoanhThuFragment();
        } else if (item.getItemId() == R.id.nav_dmk) {
            selectedFragment = new DoiMatKhauFragment();
        } else if (item.getItemId() == R.id.nav_dangxuat) {
            logout();
            return true;
        }

        // Nếu có fragment hợp lệ, load nó
        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        // Đóng Navigation Drawer sau khi chọn
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Phương thức giúp load Fragment vào container
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)  // Sử dụng replace thay vì add
                .addToBackStack(null)  // Thêm vào back stack nếu cần
                .commit();
    }

    // Ví dụ về chức năng đăng xuất
    private void logout() {
        // Thực hiện các bước đăng xuất (ví dụ: xóa session hoặc token)
        // Sau đó chuyển hướng về màn hình đăng nhập
        Intent intent = new Intent(this, Login.class); // Giả sử bạn có một màn hình đăng nhập gọi là LoginActivity
        startActivity(intent);
        finish();  // Đóng màn hình hiện tại
    }
}
