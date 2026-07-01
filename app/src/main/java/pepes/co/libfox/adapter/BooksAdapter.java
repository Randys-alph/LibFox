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

import pepes.co.libfox.BookDetailActivity;
import pepes.co.libfox.R;
import pepes.co.libfox.model.Book;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private final Context context;
    private final List<Book> books;
    private final boolean isHorizontal;

    public BooksAdapter(Context context, List<Book> books, boolean isHorizontal) {
        this.context = context;
        this.books = new java.util.ArrayList<>(books);
        this.isHorizontal = isHorizontal;
    }

    /** Replace the displayed list (used for live filtering). */
    public void updateData(List<Book> newBooks) {
        books.clear();
        books.addAll(newBooks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = isHorizontal ? R.layout.item_book_horizontal : R.layout.item_book_grid;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvRating.setText(String.valueOf(book.getRating()));

        if (book.getCoverResId() != 0) {
            holder.ivCover.setImageResource(book.getCoverResId());
        } else {
            Glide.with(context)
                    .load(book.getCoverUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .centerCrop()
                    .into(holder.ivCover);
        }

        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("book", book);
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("book", book);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return books.size(); }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvRating;
        TextView btnDetail;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvRating = itemView.findViewById(R.id.tv_rating);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
