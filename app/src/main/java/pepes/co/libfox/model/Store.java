package pepes.co.libfox.model;

import android.os.Parcel;
import java.io.Serializable;

public class Store implements Serializable {
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

    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getHours() { return hours; }
    public int getImageResId() { return imageResId; }
    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}
