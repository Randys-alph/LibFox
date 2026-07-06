package pepes.co.libfox;

import android.content.Intent;
import android.content.SharedPreferences;
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

        TextView tvGreeting = findViewById(R.id.tv_greeting);

        // Panggil file "libfox_prefs"
        SharedPreferences prefs = getSharedPreferences("libfox_prefs", MODE_PRIVATE);

        // Panggil key "username"
        String name = prefs.getString("username", "User");

        // Set ke TextView
        tvGreeting.setText("Hi, " + name);

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

        android.widget.ImageButton btnPrev = findViewById(R.id.btn_carousel_prev);
        android.widget.ImageButton btnNext = findViewById(R.id.btn_carousel_next);

        carouselImages = Arrays.asList(
                R.drawable.carousel_1,
                R.drawable.carousel_2,
                R.drawable.carousel_3
        );

        List<String> locations = Arrays.asList("Botani Square", "Malang Town Square", "Solo Paragon");
        List<String> shortTitles = Arrays.asList("Refreshing Atmosphere", "Vintage Vibes", "Classic Charm");
        List<String> longDescs = Arrays.asList(
                "Natural lighting and beautiful window views.",
                "Classic colonial architecture with an excellent collection of history books.",
                "A warm wooden interior with a quiet reading area."
        );

        CarouselAdapter adapter = new CarouselAdapter(this, carouselImages, locations, shortTitles, longDescs);
        vpCarousel.setAdapter(adapter);

        vpCarousel.setOffscreenPageLimit(3);
        androidx.viewpager2.widget.CompositePageTransformer transformer = new androidx.viewpager2.widget.CompositePageTransformer();
        transformer.addTransformer(new androidx.viewpager2.widget.MarginPageTransformer(dp(16)));
        vpCarousel.setPageTransformer(transformer);

        setupIndicators(carouselImages.size());

        // --- TRIK START DI TENGAH ---
        // Kita letakkan user di posisi tengah agar bisa bebas geser ke kiri atau kanan
        int midPoint = (Integer.MAX_VALUE / 2);
        int startPosition = midPoint - (midPoint % carouselImages.size()); // Pastikan start di gambar index 0
        vpCarousel.setCurrentItem(startPosition, false);
        updateIndicator(0);

        vpCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                // Indikator juga harus memakai modulo agar tahu indeks aslinya
                int realPosition = position % carouselImages.size();
                updateIndicator(realPosition);
            }
        });

        // Logika Klik Panah Kiri (Tinggal kurang 1 terus menerus)
        if (btnPrev != null) {
            btnPrev.setOnClickListener(v -> {
                if (vpCarousel.isFakeDragging()) return;
                resetAutoScroll();
                slowScrollToPage(vpCarousel.getCurrentItem() - 1, 800);
            });
        }

        // Logika Klik Panah Kanan (Tinggal tambah 1 terus menerus)
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (vpCarousel.isFakeDragging()) return;
                resetAutoScroll();
                slowScrollToPage(vpCarousel.getCurrentItem() + 1, 800);
            });
        }

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
                // Auto scroll juga tinggal nambah 1 terus menerus tanpa takut mentok
                int targetPage = vpCarousel.getCurrentItem() + 1;
                slowScrollToPage(targetPage, 800);

                carouselHandler.postDelayed(carouselRunnable, 5000);
            }
        };
        carouselHandler.postDelayed(carouselRunnable, 5000);
    }

    // Fungsi untuk mereset timer otomatis saat user menekan panah manual
    private void resetAutoScroll() {
        if (carouselHandler != null && carouselRunnable != null) {
            carouselHandler.removeCallbacks(carouselRunnable); // Hapus antrean yang lama
            carouselHandler.postDelayed(carouselRunnable, 5000); // Mulai hitung mundur 5 detik baru
        }
    }

    // Fungsi untuk memperlambat animasi geser ViewPager2
    private void slowScrollToPage(int page, long durationMs) {
        if (vpCarousel.isFakeDragging()) return;

        int currentItem = vpCarousel.getCurrentItem();
        if (page == currentItem) return;

        // Hitung jarak piksel yang harus digeser (sebesar lebar ViewPager)
        int pxToDrag = vpCarousel.getWidth();
        if (page < currentItem) {
            pxToDrag = -pxToDrag;
        }

        android.animation.ValueAnimator animator = android.animation.ValueAnimator.ofInt(0, pxToDrag);
        animator.setDuration(durationMs); // Mengatur kecepatan geser
        animator.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());

        final int[] previousValue = {0};

        animator.addUpdateListener(valueAnimator -> {
            int currentValue = (int) valueAnimator.getAnimatedValue();
            float currentPxToDrag = (float) (currentValue - previousValue[0]);
            vpCarousel.fakeDragBy(-currentPxToDrag);
            previousValue[0] = currentValue;
        });

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
                vpCarousel.beginFakeDrag();
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                vpCarousel.endFakeDrag();
                // Memastikan halaman benar-benar pindah ke target setelah animasi selesai
                vpCarousel.setCurrentItem(page, false);
            }
        });

        animator.start();
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
