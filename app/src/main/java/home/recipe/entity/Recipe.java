package home.recipe.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by greg on 12.09.15.
 */
public class Recipe implements Parcelable {
    private long id;
    private float rank;
    private String publisherName;
    private String f2f_url;
    private String title;
    private String source_url;
    private String image_url;
    private String publisher_url;

    public Recipe() {
    }
    private Recipe(Parcel in){
        long id = in.readLong();
        float rank = in.readFloat();
         String publisherName = in.readString();
         String f2f_url = in.readString();
         String title = in.readString();
         String source_url = in.readString();
         String image_url = in.readString();
         String publisher_url = in.readString();
    }

    public String getF2f_url() {
        return f2f_url;
    }

    public void setF2f_url(String f2f_url) {
        this.f2f_url = f2f_url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(rank);
        dest.writeString(title);
        dest.writeString(publisher_url);
        dest.writeString(publisherName);
        dest.writeString(f2f_url);
        dest.writeString(image_url);
        dest.writeString(source_url);
    }
}
