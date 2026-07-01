package pepes.co.libfox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pepes.co.libfox.model.Store;

public class StoreDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        Store store = getIntent().getParcelableExtra("store");
        if (store == null) {
            finish();
            return;
        }

        ImageView ivHeader = findViewById(R.id.iv_header);
        TextView tvName = findViewById(R.id.tv_store_name);
        TextView tvAddress = findViewById(R.id.tv_store_address);
        TextView tvDescription = findViewById(R.id.tv_description);

        ivHeader.setImageResource(store.getImageResId());
        tvName.setText(store.getName());
        tvAddress.setText(store.getAddress());
        tvDescription.setText(store.getDescription());

        // Tap the dimmed area outside the sheet to dismiss
        findViewById(R.id.store_detail_root).setOnClickListener(v -> finish());

        Button btnDirections = findViewById(R.id.btn_directions);
        btnDirections.setOnClickListener(v -> openMaps(store));
    }

    private void openMaps(Store store) {
        try {
            Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(store.getName() + ", " + store.getAddress()));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        } catch (Exception e) {
            Toast.makeText(this, "No maps app available", Toast.LENGTH_SHORT).show();
        }
    }
}
