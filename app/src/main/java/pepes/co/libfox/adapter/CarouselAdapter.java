package pepes.co.libfox.adapter;

import android.content.Context;
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

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private final Context context;
    private final List<Integer> imageResIds;
    private final List<String> locations;
    private final List<String> shortTitles;
    private final List<String> longDescs;

    // Tambahkan variabel baru ke dalam constructor
    public CarouselAdapter(Context context, List<Integer> imageResIds, List<String> locations, List<String> shortTitles, List<String> longDescs) {
        this.context = context;
        this.imageResIds = imageResIds;
        this.locations = locations;
        this.shortTitles = shortTitles;
        this.longDescs = longDescs;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        int realPosition = position % imageResIds.size();

        Glide.with(context)
                .load(imageResIds.get(realPosition))
                .centerCrop()
                .into(holder.ivSlide);

        // Pasang ketiga teks ke posisinya masing-masing
        if (locations != null && !locations.isEmpty()) {
            holder.tvLocation.setText(locations.get(realPosition));
            holder.tvShortTitle.setText(shortTitles.get(realPosition));
            holder.tvLongDesc.setText(longDescs.get(realPosition));
        }
    }

    @Override
    public int getItemCount() { return Integer.MAX_VALUE; }

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlide;
        TextView tvLocation, tvShortTitle, tvLongDesc;

        CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlide = itemView.findViewById(R.id.iv_slide);
            tvLocation = itemView.findViewById(R.id.tv_location); // Binding ID baru
            tvShortTitle = itemView.findViewById(R.id.tv_short_title); // Binding ID baru
            tvLongDesc = itemView.findViewById(R.id.tv_long_desc); // Binding ID baru
        }
    }
}
