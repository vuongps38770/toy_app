package com.project1.toystoreapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.project1.toystoreapp.Fragment.DangXuatFragment;
import com.project1.toystoreapp.Fragment.DoanhThuFragment;
import com.project1.toystoreapp.Fragment.DoiMatKhauFragment;
import com.project1.toystoreapp.Fragment.GioHangFragment;
import com.project1.toystoreapp.Fragment.HomeFragment;
import com.project1.toystoreapp.Fragment.QLSPFragment;
import com.project1.toystoreapp.Fragment.XacThucFragment;
import com.project1.toystoreapp.R;
import androidx.appcompat.widget.Toolbar;

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

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Thiết lập ActionBarDrawerToggle để mở/đóng Navigation Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Nếu savedInstanceState là null, chọn fragment mặc định là HomeFragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
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
        }else if (item.getItemId() == R.id.nav_xacthuc) {
            selectedFragment = new XacThucFragment();
        }else if (item.getItemId() == R.id.nav_doanhthu) {
            selectedFragment = new DoanhThuFragment();
        }else if (item.getItemId() == R.id.nav_dmk) {
            selectedFragment = new DoiMatKhauFragment();
        }else if (item.getItemId() == R.id.nav_dangxuat) {
            selectedFragment = new DangXuatFragment();
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
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)  // Thêm vào back stack nếu cần
                .commit();
    }
}
