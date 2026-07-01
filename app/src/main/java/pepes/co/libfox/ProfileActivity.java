package pepes.co.libfox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "libfox_prefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(KEY_USERNAME, "Tiny Guz");

        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(username);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> MenuHelper.logout(this));
    }
}
