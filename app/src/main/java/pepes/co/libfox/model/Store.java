package pepes.co.libfox.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Store implements Parcelable {
    private String name;
    private String address;
    private String hours;
    private int imageResId;
    private String description;

    public Store(String name, String address, String hours, int imageResId, String description) {
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.imageResId = imageResId;
        this.description = description;
    }

    public Store(String name, String address, String hours, int imageResId) {
        this(name, address, hours, imageResId, "");
    }

    protected Store(Parcel in) {
        name = in.readString();
        address = in.readString();
        hours = in.readString();
        imageResId = in.readInt();
        description = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) { return new Store(in); }
        @Override
        public Store[] newArray(int size) { return new Store[size]; }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(hours);
        dest.writeInt(imageResId);
        dest.writeString(description);
    }

    @Override
    public int describeContents() { return 0; }

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getHours() { return hours; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }
}
