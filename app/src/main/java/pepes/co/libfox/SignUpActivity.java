package pepes.co.libfox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "libfox_prefs";
    private static final String KEY_USERNAME = "username";
    private static final Pattern ALPHANUM = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");

    private EditText etFullName, etEmail, etUsername, etPassword, etConfirm;
    private CheckBox cbTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFullName = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirm = findViewById(R.id.et_confirm_password);
        cbTerms = findViewById(R.id.cb_terms);

        Button btnSignup = findViewById(R.id.btn_signup);
        btnSignup.setOnTouchListener((v, event) -> {
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
        btnSignup.setOnClickListener(v -> attemptSignUp());

        findViewById(R.id.tv_signin).setOnClickListener(v -> finish());
        findViewById(R.id.btn_google).setOnClickListener(v ->
                Toast.makeText(this, "Sign up with Google", Toast.LENGTH_SHORT).show());
        findViewById(R.id.btn_apple).setOnClickListener(v ->
                Toast.makeText(this, "Sign up with Apple", Toast.LENGTH_SHORT).show());
    }

    private void attemptSignUp() {
        String name = text(etFullName);
        String email = text(etEmail);
        String username = text(etUsername);
        String password = text(etPassword);
        String confirm = text(etConfirm);

        if (name.isEmpty()) { fail(etFullName, "Full name is required"); return; }
        if (email.isEmpty()) { fail(etEmail, "Email is required"); return; }
        if (!EMAIL.matcher(email).matches()) { fail(etEmail, "Invalid email format"); return; }
        if (username.isEmpty()) { fail(etUsername, getString(R.string.error_username_empty)); return; }
        if (password.isEmpty()) { fail(etPassword, getString(R.string.error_password_empty)); return; }
        if (!ALPHANUM.matcher(password).matches()) { fail(etPassword, getString(R.string.error_password_format)); return; }
        if (!confirm.equals(password)) { fail(etConfirm, "Passwords do not match"); return; }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to the Terms of Service", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_USERNAME, username).apply();

        Toast.makeText(this, "Account created. Welcome to LibFox!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private String text(EditText e) {
        return e.getText() != null ? e.getText().toString().trim() : "";
    }

    private void fail(EditText e, String msg) {
        e.setError(msg);
        e.requestFocus();
    }
}
