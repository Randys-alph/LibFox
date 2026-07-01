package pepes.co.libfox.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String title;
    private String author;
    private float rating;
    private String price;
    private String coverUrl;
    private int coverResId;
    private String category;
    private String description;

    public Book(String title, String author, float rating, String price,
                int coverResId, String category, String description) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.price = price;
        this.coverUrl = "";
        this.coverResId = coverResId;
        this.category = category;
        this.description = description;
    }

    public Book(String title, String author, float rating, String price,
                String coverUrl, String category, String description) {
        this.title = title;
        this.author = author;
        this.rating = rating;
        this.price = price;
        this.coverUrl = coverUrl;
        this.coverResId = 0;
        this.category = category;
        this.description = description;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        rating = in.readFloat();
        price = in.readString();
        coverUrl = in.readString();
        coverResId = in.readInt();
        category = in.readString();
        description = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) { return new Book(in); }
        @Override
        public Book[] newArray(int size) { return new Book[size]; }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeFloat(rating);
        dest.writeString(price);
        dest.writeString(coverUrl);
        dest.writeInt(coverResId);
        dest.writeString(category);
        dest.writeString(description);
    }

    @Override
    public int describeContents() { return 0; }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public float getRating() { return rating; }
    public String getPrice() { return price; }
    public String getCoverUrl() { return coverUrl; }
    public int getCoverResId() { return coverResId; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
}
