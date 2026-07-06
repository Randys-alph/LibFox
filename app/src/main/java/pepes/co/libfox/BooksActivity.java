package pepes.co.libfox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pepes.co.libfox.adapter.BooksAdapter;
import pepes.co.libfox.model.DataHelper;

public class BooksActivity extends AppCompatActivity {

    private TextView chipAll, chipFiction, chipNonFiction;
    private TextView tvSeeAllFiction, tvSeeAllNonFiction;
    private LinearLayout llFictionSection, llNonFictionSection;
    private RecyclerView rvFiction, rvNonFiction;
    private BooksAdapter fictionAdapter, nonFictionAdapter;

    private String currentCategory = "All";
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // Avatar (fox) → logout menu
        ImageView ivAvatar = findViewById(R.id.iv_avatar);
        ivAvatar.setImageResource(R.drawable.mascot_fox);
        ivAvatar.setOnClickListener(v -> MenuHelper.showProfileMenu(this, v));

        // Setup sections & views
        llFictionSection = findViewById(R.id.ll_fiction_section);
        llNonFictionSection = findViewById(R.id.ll_nonfiction_section);
        rvFiction = findViewById(R.id.rv_fiction);
        rvNonFiction = findViewById(R.id.rv_nonfiction);

        // ID ini di activity_books.xml pada teks "See All" untuk masing-masing genre
        tvSeeAllFiction = findViewById(R.id.tv_see_all_fiction);
        tvSeeAllNonFiction = findViewById(R.id.tv_see_all_nonfiction);

        // Setup Filter Chips (Modular seperti di HomeActivity)
        setupFilterChips();

        // Logika Klik "See All" (Mensimulasikan klik pada Chip kategori terkait)
        if (tvSeeAllFiction != null) {
            tvSeeAllFiction.setOnClickListener(v -> chipFiction.performClick());
        }
        if (tvSeeAllNonFiction != null) {
            tvSeeAllNonFiction.setOnClickListener(v -> chipNonFiction.performClick());
        }

        // Search filtering
        setupSearch();

        // Bottom navigation
        setupBottomNav();

        // Panggil saat activity pertama kali dibuat agar layout tersetting
        applyFilter();
    }

    // Method khusus untuk setup Chips
    private void setupFilterChips() {
        // Inisialisasi menggunakan variabel class (field)
        chipAll = findViewById(R.id.chip_all);
        chipFiction = findViewById(R.id.chip_fiction);
        chipNonFiction = findViewById(R.id.chip_nonfiction);

        // Set default awal
        chipAll.setSelected(true);

        View.OnClickListener chipListener = v -> {
            // 1. Reset semua ke status false (Selector XML akan otomatis jadi Inactive)
            chipAll.setSelected(false);
            chipFiction.setSelected(false);
            chipNonFiction.setSelected(false);

            // 2. Set yang diklik ke status true (Selector XML akan otomatis jadi Active/Orange)
            v.setSelected(true);

            // 3. Update kategori untuk filter
            int id = v.getId();
            if (id == R.id.chip_all) {
                currentCategory = "All";
            } else if (id == R.id.chip_fiction) {
                currentCategory = "Fiction";
            } else if (id == R.id.chip_nonfiction) {
                currentCategory = "Non-Fiction";
            }

            applyFilter();
        };

        chipAll.setOnClickListener(chipListener);
        chipFiction.setOnClickListener(chipListener);
        chipNonFiction.setOnClickListener(chipListener);
    }

    // Fungsi utama untuk mengatur UI berdasarkan genre yang dipilih
    private void applyFilter() {
        if ("All".equals(currentCategory)) {
            // Tampilkan kedua section
            llFictionSection.setVisibility(View.VISIBLE);
            llNonFictionSection.setVisibility(View.VISIBLE);

            // Tampilkan kembali tombol "See All"
            if (tvSeeAllFiction != null) tvSeeAllFiction.setVisibility(View.VISIBLE);
            if (tvSeeAllNonFiction != null) tvSeeAllNonFiction.setVisibility(View.VISIBLE);

            // Set Layout Manager menjadi Horizontal & update parameter boolean Adapter menjadi 'true'
            rvFiction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            fictionAdapter = new BooksAdapter(this, filter(DataHelper.getFictionBooks(), currentQuery), true);
            rvFiction.setAdapter(fictionAdapter);

            rvNonFiction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            nonFictionAdapter = new BooksAdapter(this, filter(DataHelper.getNonFictionBooks(), currentQuery), true);
            rvNonFiction.setAdapter(nonFictionAdapter);

        } else if ("Fiction".equals(currentCategory)) {
            // Hanya tampilkan Fiction
            llFictionSection.setVisibility(View.VISIBLE);
            llNonFictionSection.setVisibility(View.GONE);

            // Sembunyikan "See All"
            if (tvSeeAllFiction != null) tvSeeAllFiction.setVisibility(View.GONE);

            // Set Layout Manager menjadi Grid 2 Kolom & update parameter boolean Adapter menjadi 'false'
            rvFiction.setLayoutManager(new GridLayoutManager(this, 2));
            fictionAdapter = new BooksAdapter(this, filter(DataHelper.getFictionBooks(), currentQuery), false);
            rvFiction.setAdapter(fictionAdapter);

        } else if ("Non-Fiction".equals(currentCategory)) {
            // Hanya tampilkan Non-Fiction
            llFictionSection.setVisibility(View.GONE);
            llNonFictionSection.setVisibility(View.VISIBLE);

            // Sembunyikan "See All"
            if (tvSeeAllNonFiction != null) tvSeeAllNonFiction.setVisibility(View.GONE);

            // Set Layout Manager menjadi Grid 2 Kolom & update parameter boolean Adapter menjadi 'false'
            rvNonFiction.setLayoutManager(new GridLayoutManager(this, 2));
            nonFictionAdapter = new BooksAdapter(this, filter(DataHelper.getNonFictionBooks(), currentQuery), false);
            rvNonFiction.setAdapter(nonFictionAdapter);
        }
    }

    private void setupSearch() {
        android.widget.EditText etSearch = findViewById(R.id.et_search);
        if (etSearch == null) return;
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                currentQuery = s.toString().trim().toLowerCase();
                // Panggil ulang applyFilter agar pencarian menyesuaikan view saat ini (Grid/Horizontal)
                applyFilter();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) { }
        });
    }

    private java.util.List<pepes.co.libfox.model.Book> filter(
            java.util.List<pepes.co.libfox.model.Book> source, String q) {
        if (q.isEmpty()) return source;
        java.util.List<pepes.co.libfox.model.Book> out = new java.util.ArrayList<>();
        for (pepes.co.libfox.model.Book b : source) {
            if (b.getTitle().toLowerCase().contains(q)
                    || b.getAuthor().toLowerCase().contains(q)) {
                out.add(b);
            }
        }
        return out;
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_category);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_category) {
                return true;
            } else if (id == R.id.nav_store) {
                startActivity(new Intent(this, StoresActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}