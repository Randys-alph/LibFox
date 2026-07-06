package pepes.co.libfox.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import pepes.co.libfox.R;
import pepes.co.libfox.StoreDetailFragment;
import pepes.co.libfox.model.Store;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.StoreViewHolder> {

    private final Context context;
    private final List<Store> stores;

    public StoresAdapter(Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = stores.get(position);
        holder.tvName.setText(store.getName());
        holder.tvAddress.setText(store.getAddress());
        holder.tvHours.setText(store.getHours());

        Glide.with(context)
                .load(store.getImageResId())
                .centerCrop()
                .into(holder.ivStore);

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof androidx.fragment.app.FragmentActivity) {
                // Gunakan static method newInstance di Fragment
                StoreDetailFragment fragment = StoreDetailFragment.newInstance(store);

                // Tampilkan BottomSheet
                fragment.show(((androidx.fragment.app.FragmentActivity) context)
                        .getSupportFragmentManager(), "StoreDetailTag");
            }
        });

    }

    @Override
    public int getItemCount() { return stores.size(); }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStore;
        TextView tvName, tvAddress, tvHours;

        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStore = itemView.findViewById(R.id.iv_store);
            tvName = itemView.findViewById(R.id.tv_store_name);
            tvAddress = itemView.findViewById(R.id.tv_store_address);
            tvHours = itemView.findViewById(R.id.tv_store_hours);
        }
    }
}
