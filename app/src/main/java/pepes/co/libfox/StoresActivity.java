package pepes.co.libfox;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pepes.co.libfox.adapter.StoresAdapter;
import pepes.co.libfox.model.DataHelper;

public class StoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        // Avatar (fox) → Profile / Log Out dropdown
        ImageView ivAvatar = findViewById(R.id.iv_avatar);
        ivAvatar.setImageResource(R.drawable.mascot_fox);
        ivAvatar.setOnClickListener(v -> MenuHelper.showProfileMenu(this, v));

        // Stores list
        RecyclerView rvStores = findViewById(R.id.rv_stores);
        rvStores.setLayoutManager(new LinearLayoutManager(this));
        rvStores.setAdapter(new StoresAdapter(this, DataHelper.getStores()));

        // Bottom navigation
        setupBottomNav();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_store);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_category) {
                startActivity(new Intent(this, BooksActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_store) {
                return true;
            }
            return false;
        });
    }
}
