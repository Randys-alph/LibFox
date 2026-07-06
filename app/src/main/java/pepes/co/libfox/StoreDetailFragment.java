package pepes.co.libfox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import pepes.co.libfox.model.Store;

public class StoreDetailFragment extends BottomSheetDialogFragment {

    private Store store;
    private ImageView ivHeader;
    private TextView tvStoreName;
    private TextView tvStoreAddress;
    private TextView tvDescription;
    private Button btnDirections;

    // Gunakan static method agar data aman saat fragment di-recreate sistem
    public static StoreDetailFragment newInstance(Store store) {
        StoreDetailFragment fragment = new StoreDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("store", store);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout yang sudah di buat
        return inflater.inflate(R.layout.activity_store_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View binding manual yang kompatibel
        ivHeader = view.findViewById(R.id.iv_header);
        tvStoreName = view.findViewById(R.id.tv_store_name);
        tvStoreAddress= view.findViewById(R.id.tv_store_address);
        tvDescription = view.findViewById(R.id.tv_description);
        btnDirections = view.findViewById(R.id.btn_directions);

        if (getArguments() != null) {
            store = (Store) getArguments().getSerializable("store");
            if (store != null) {
                // Populate data
                ivHeader.setImageResource(store.getImageResId());
                tvStoreName.setText(store.getName());
                tvStoreAddress.setText(store.getAddress());
                tvDescription.setText(store.getDescription());

                btnDirections.setOnClickListener(v -> openMaps(store));
            }
        }

        if (store == null) return;
    }

    private void openMaps(Store store) {
        try {
            // Intent ini standar dan bisa digunakan di semua versi SDK yang kamu set (minSdk 26)
            Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(store.getName() + ", " + store.getAddress()));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Maps tidak tersedia", Toast.LENGTH_SHORT).show();
        }
    }
}