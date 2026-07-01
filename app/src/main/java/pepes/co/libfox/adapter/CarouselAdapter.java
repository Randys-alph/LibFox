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
    private final List<String> captions;

    public CarouselAdapter(Context context, List<Integer> imageResIds, List<String> captions) {
        this.context = context;
        this.imageResIds = imageResIds;
        this.captions = captions;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        Glide.with(context)
                .load(imageResIds.get(position))
                .centerCrop()
                .into(holder.ivSlide);

        if (holder.tvCaption != null && captions != null && position < captions.size()) {
            holder.tvCaption.setText(captions.get(position));
        }
    }

    @Override
    public int getItemCount() { return imageResIds.size(); }

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSlide;
        TextView tvCaption;

        CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlide = itemView.findViewById(R.id.iv_slide);
            tvCaption = itemView.findViewById(R.id.tv_caption);
        }
    }
}
