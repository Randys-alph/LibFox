package pepes.co.libfox;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.regex.Pattern;

import pepes.co.libfox.adapter.BooksAdapter;
import pepes.co.libfox.model.Book;
import pepes.co.libfox.model.DataHelper;

public class BookDetailActivity extends AppCompatActivity {

    private static final Pattern NUMERIC = Pattern.compile("^[0-9]+$");

    private EditText etAddress, etPhone;
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        currentBook = getIntent().getParcelableExtra("book");
        if (currentBook == null) {
            finish();
            return;
        }

        // Back button
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Book info
        ImageView ivCover = findViewById(R.id.iv_book_cover);
        TextView tvTitle = findViewById(R.id.tv_book_title);
        TextView tvAuthor = findViewById(R.id.tv_author);
        TextView tvRating = findViewById(R.id.tv_rating);
        TextView tvCategory = findViewById(R.id.tv_category);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvDescription = findViewById(R.id.tv_description);
        TextView tvSubtotal = findViewById(R.id.tv_subtotal);
        TextView tvTotal = findViewById(R.id.tv_total);

        if (currentBook.getCoverResId() != 0) {
            ivCover.setImageResource(currentBook.getCoverResId());
        } else {
            Glide.with(this)
                    .load(currentBook.getCoverUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .centerCrop()
                    .into(ivCover);
        }

        tvTitle.setText(currentBook.getTitle());
        tvAuthor.setText(currentBook.getAuthor());
        tvRating.setText(String.valueOf(currentBook.getRating()));
        tvCategory.setText(currentBook.getCategory());
        tvPrice.setText(currentBook.getPrice());
        tvDescription.setText(currentBook.getDescription());
        tvSubtotal.setText(currentBook.getPrice());
        tvTotal.setText(currentBook.getPrice());

        // Form fields
        etAddress = findViewById(R.id.et_address);
        etPhone = findViewById(R.id.et_phone);

        // Buy Now button with touch-down color change
        Button btnBuy = findViewById(R.id.btn_buy);
        btnBuy.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                btnBuy.setBackgroundResource(R.drawable.btn_orange_selector);
                v.setAlpha(0.75f);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setAlpha(1.0f);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
            }
            return true;
        });

        btnBuy.setOnClickListener(v -> validateAndSubmit());

        // Order Book → reveal the Secure Purchase checkout form
        Button btnOrder = findViewById(R.id.btn_order);
        LinearLayout llCheckout = findViewById(R.id.ll_checkout);
        NestedScrollView scrollView = findViewById(R.id.scroll_view);
        btnOrder.setOnClickListener(v -> {
            btnOrder.setVisibility(View.GONE);
            llCheckout.setVisibility(View.VISIBLE);
            llCheckout.post(() -> scrollView.smoothScrollTo(0, llCheckout.getTop()));
            etAddress.requestFocus();
        });

        // Featured books
        RecyclerView rvFeatured = findViewById(R.id.rv_featured);
        rvFeatured.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFeatured.setAdapter(new BooksAdapter(this,
                new ArrayList<>(DataHelper.getFeaturedBooks()), true));

        TextView tvSeeAll = findViewById(R.id.tv_see_all_detail);
        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> startActivity(new Intent(this, BooksActivity.class)));
        }

        // Bottom navigation
        setupBottomNav();
    }

    private void validateAndSubmit() {
        String address = etAddress.getText() != null ? etAddress.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";

        if (address.isEmpty()) {
            showErrorDialog(getString(R.string.error_address_empty));
            return;
        }

        if (phone.isEmpty()) {
            showErrorDialog(getString(R.string.error_phone_empty));
            return;
        }

        if (!NUMERIC.matcher(phone).matches()) {
            showErrorDialog(getString(R.string.error_phone_format));
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_success_title))
                .setMessage(getString(R.string.success_order))
                .setPositiveButton(getString(R.string.dialog_ok), (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_error_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_ok), null)
                .show();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_category);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_category) {
                startActivity(new Intent(this, BooksActivity.class));
                return true;
            } else if (id == R.id.nav_store) {
                startActivity(new Intent(this, StoresActivity.class));
                return true;
            }
            return false;
        });
    }
}
