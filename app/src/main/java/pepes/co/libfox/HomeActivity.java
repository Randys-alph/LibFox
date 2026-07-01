package pepes.co.libfox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pepes.co.libfox.adapter.BooksAdapter;
import pepes.co.libfox.adapter.CarouselAdapter;
import pepes.co.libfox.model.Book;
import pepes.co.libfox.model.DataHelper;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 vpCarousel;
    private LinearLayout llIndicators;
    private List<Integer> carouselImages;
    private Handler carouselHandler;
    private Runnable carouselRunnable;
    private int currentPage = 0;

    private RecyclerView rvFeatured, rvGrid;
    private BooksAdapter featuredAdapter, gridAdapter;
    private List<Book> allBooks;

    private TextView chipAll, chipFiction, chipNonFiction;
    private String currentCategory = "All";
    private String currentQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Avatar (fox) → logout menu
        ImageView ivAvatar = findViewById(R.id.iv_avatar);
        ivAvatar.setImageResource(R.drawable.mascot_fox);
        ivAvatar.setOnClickListener(v -> MenuHelper.showProfileMenu(this, v));

        // Setup carousel
        setupCarousel();

        // Setup books data
        allBooks = DataHelper.getAllBooks();
        setupFeaturedBooks();
        setupBooksGrid(allBooks);
        setupFilterChips();
        setupSearch();

        // Bottom navigation
        setupBottomNav(R.id.nav_home);

        // See All Featured → go to Books Activity
        TextView tvSeeAll = findViewById(R.id.tv_see_all_featured);
        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> goToBooks());
        }
    }

    private void setupCarousel() {
        vpCarousel = findViewById(R.id.vp_carousel);
        llIndicators = findViewById(R.id.ll_indicators);

        carouselImages = Arrays.asList(
                R.drawable.carousel_1,
                R.drawable.carousel_2,
                R.drawable.carousel_3
        );

        List<String> captions = Arrays.asList("Botani Square", "Lokwood Co", "Solo Paragon");

        CarouselAdapter adapter = new CarouselAdapter(this, carouselImages, captions);
        vpCarousel.setAdapter(adapter);

        setupIndicators(carouselImages.size());
        updateIndicator(0);

        vpCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updateIndicator(position);
            }
        });

        startAutoScroll();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void setupIndicators(int count) {
        llIndicators.removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(dp(8), dp(8));
            params.setMargins(dp(3), 0, dp(3), 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.bg_indicator_inactive);
            llIndicators.addView(dot);
        }
    }

    private void updateIndicator(int position) {
        for (int i = 0; i < llIndicators.getChildCount(); i++) {
            View dot = llIndicators.getChildAt(i);
            LinearLayout.LayoutParams params =
                    (LinearLayout.LayoutParams) dot.getLayoutParams();
            if (i == position) {
                params.width = dp(20);
                dot.setBackgroundResource(R.drawable.bg_indicator_active);
            } else {
                params.width = dp(8);
                dot.setBackgroundResource(R.drawable.bg_indicator_inactive);
            }
            dot.setLayoutParams(params);
        }
    }

    private void startAutoScroll() {
        carouselHandler = new Handler(Looper.getMainLooper());
        carouselRunnable = () -> {
            if (carouselImages != null && !carouselImages.isEmpty()) {
                currentPage = (currentPage + 1) % carouselImages.size();
                vpCarousel.setCurrentItem(currentPage, true);
                carouselHandler.postDelayed(carouselRunnable, 3000);
            }
        };
        carouselHandler.postDelayed(carouselRunnable, 3000);
    }

    private void setupFeaturedBooks() {
        rvFeatured = findViewById(R.id.rv_featured_books);
        List<Book> featured = DataHelper.getFeaturedBooks();
        featuredAdapter = new BooksAdapter(this, new ArrayList<>(featured), true);
        rvFeatured.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFeatured.setAdapter(featuredAdapter);
    }

    private void setupBooksGrid(List<Book> books) {
        rvGrid = findViewById(R.id.rv_books_grid);
        gridAdapter = new BooksAdapter(this, books, false);
        rvGrid.setLayoutManager(new GridLayoutManager(this, 2));
        rvGrid.setAdapter(gridAdapter);
    }

    private void setupFilterChips() {
        chipAll = findViewById(R.id.chip_all);
        chipFiction = findViewById(R.id.chip_fiction);
        chipNonFiction = findViewById(R.id.chip_nonfiction);

        chipAll.setOnClickListener(v -> {
            setChipActive(chipAll);
            setChipInactive(chipFiction);
            setChipInactive(chipNonFiction);
            currentCategory = "All";
            applyFilter();
        });

        chipFiction.setOnClickListener(v -> {
            setChipActive(chipFiction);
            setChipInactive(chipAll);
            setChipInactive(chipNonFiction);
            currentCategory = "Fiction";
            applyFilter();
        });

        chipNonFiction.setOnClickListener(v -> {
            setChipActive(chipNonFiction);
            setChipInactive(chipAll);
            setChipInactive(chipFiction);
            currentCategory = "Non-Fiction";
            applyFilter();
        });
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
                applyFilter();
            }
            @Override
            public void afterTextChanged(android.text.Editable s) { }
        });
    }

    private void applyFilter() {
        List<Book> base;
        if ("Fiction".equals(currentCategory)) {
            base = DataHelper.getFictionBooks();
        } else if ("Non-Fiction".equals(currentCategory)) {
            base = DataHelper.getNonFictionBooks();
        } else {
            base = allBooks;
        }

        List<Book> result = new ArrayList<>();
        for (Book b : base) {
            if (currentQuery.isEmpty()
                    || b.getTitle().toLowerCase().contains(currentQuery)
                    || b.getAuthor().toLowerCase().contains(currentQuery)) {
                result.add(b);
            }
        }
        gridAdapter.updateData(result);
    }

    private void setChipActive(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_chip_active);
        chip.setTextColor(getResources().getColor(R.color.white, getTheme()));
    }

    private void setChipInactive(TextView chip) {
        chip.setBackgroundResource(R.drawable.bg_chip_inactive);
        chip.setTextColor(getResources().getColor(R.color.primary, getTheme()));
    }

    private void setupBottomNav(int selectedId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(selectedId);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_category) {
                goToBooks();
                return true;
            } else if (id == R.id.nav_store) {
                goToStores();
                return true;
            }
            return false;
        });
    }

    private void goToBooks() {
        startActivity(new Intent(this, BooksActivity.class));
    }

    private void goToStores() {
        startActivity(new Intent(this, StoresActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (carouselHandler != null && carouselRunnable != null) {
            carouselHandler.removeCallbacks(carouselRunnable);
        }
    }
}
