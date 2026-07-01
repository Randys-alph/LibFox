package pepes.co.libfox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pepes.co.libfox.adapter.BooksAdapter;
import pepes.co.libfox.model.DataHelper;

public class BooksActivity extends AppCompatActivity {

    private TextView chipAll, chipFiction, chipNonFiction;
    private LinearLayout llFictionSection, llNonFictionSection;
    private RecyclerView rvFiction, rvNonFiction;
    private BooksAdapter fictionAdapter, nonFictionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // Avatar (fox) → logout menu
        ImageView ivAvatar = findViewById(R.id.iv_avatar);
        ivAvatar.setImageResource(R.drawable.mascot_fox);
        ivAvatar.setOnClickListener(v -> MenuHelper.showProfileMenu(this, v));

        // Setup sections
        llFictionSection = findViewById(R.id.ll_fiction_section);
        llNonFictionSection = findViewById(R.id.ll_nonfiction_section);
        rvFiction = findViewById(R.id.rv_fiction);
        rvNonFiction = findViewById(R.id.rv_nonfiction);

        rvFiction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fictionAdapter = new BooksAdapter(this, DataHelper.getFictionBooks(), true);
        rvFiction.setAdapter(fictionAdapter);

        rvNonFiction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        nonFictionAdapter = new BooksAdapter(this, DataHelper.getNonFictionBooks(), true);
        rvNonFiction.setAdapter(nonFictionAdapter);

        // Filter chips
        chipAll = findViewById(R.id.chip_all);
        chipFiction = findViewById(R.id.chip_fiction);
        chipNonFiction = findViewById(R.id.chip_nonfiction);

        chipAll.setOnClickListener(v -> {
            setActive(chipAll);
            setInactive(chipFiction);
            setInactive(chipNonFiction);
            llFictionSection.setVisibility(View.VISIBLE);
            llNonFictionSection.setVisibility(View.VISIBLE);
        });

        chipFiction.setOnClickListener(v -> {
            setActive(chipFiction);
            setInactive(chipAll);
            setInactive(chipNonFiction);
            llFictionSection.setVisibility(View.VISIBLE);
            llNonFictionSection.setVisibility(View.GONE);
        });

        chipNonFiction.setOnClickListener(v -> {
            setActive(chipNonFiction);
            setInactive(chipAll);
            setInactive(chipFiction);
            llFictionSection.setVisibility(View.GONE);
            llNonFictionSection.setVisibility(View.VISIBLE);
        });

        // Search filtering
        setupSearch();

        // Bottom navigation
        setupBottomNav();
    }

    private void setupSearch() {
        android.widget.EditText etSearch = findViewById(R.id.et_search);
        if (etSearch == null) return;
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                String q = s.toString().trim().toLowerCase();
                fictionAdapter.updateData(filter(DataHelper.getFictionBooks(), q));
                nonFictionAdapter.updateData(filter(DataHelper.getNonFictionBooks(), q));
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

    private void setActive(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_chip_active);
        chip.setTextColor(getResources().getColor(R.color.white, getTheme()));
    }

    private void setInactive(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_chip_inactive);
        chip.setTextColor(getResources().getColor(R.color.primary, getTheme()));
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
