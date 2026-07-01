package pepes.co.libfox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "libfox_prefs";
    private static final String KEY_USERNAME = "username";
    private static final Pattern ALPHANUM = Pattern.compile("^[a-zA-Z0-9]+$");

    private EditText etUsername, etPassword;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Skip login if already logged in
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.contains(KEY_USERNAME)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        ImageView toggle = findViewById(R.id.iv_toggle_password);
        toggle.setOnClickListener(v -> togglePassword());

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setAlpha(0.85f);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setAlpha(1.0f);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
            }
            return true;
        });
        btnLogin.setOnClickListener(v -> attemptLogin());

        TextView tvSignup = findViewById(R.id.tv_signup);
        tvSignup.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));

        TextView tvForgot = findViewById(R.id.tv_forgot);
        tvForgot.setOnClickListener(v ->
                Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btn_google).setOnClickListener(v ->
                Toast.makeText(this, "Sign in with Google", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_apple).setOnClickListener(v ->
                Toast.makeText(this, "Sign in with Apple", Toast.LENGTH_SHORT).show());
    }

    private void togglePassword() {
        passwordVisible = !passwordVisible;
        int sel = etPassword.getSelectionEnd();
        if (passwordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        etPassword.setSelection(sel);
        ImageView toggle = findViewById(R.id.iv_toggle_password);
        toggle.setImageResource(passwordVisible ? R.drawable.ic_eye_off : R.drawable.ic_eye);
        toggle.setColorFilter(ContextCompat.getColor(this, R.color.gray));
    }

    private void attemptLogin() {
        String username = etUsername.getText() != null
                ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null
                ? etPassword.getText().toString() : "";

        if (username.isEmpty()) {
            etUsername.setError(getString(R.string.error_username_empty));
            etUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.error_password_empty));
            etPassword.requestFocus();
            return;
        }
        if (!ALPHANUM.matcher(password).matches()) {
            etPassword.setError(getString(R.string.error_password_format));
            etPassword.requestFocus();
            return;
        }

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_USERNAME, username)
                .apply();

        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
