
package com.example.alex.popularmoviess1.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Trailers implements Parcelable
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private List<Trailer> trailers = null;
    public final static Parcelable.Creator<Trailers> CREATOR = new Creator<Trailers>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }

        public Trailers[] newArray(int size) {
            return (new Trailers[size]);
        }

    }
    ;

    private Trailers(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        trailers = new ArrayList<>();
        in.readList(this.trailers, (Trailer.class.getClassLoader()));
    }

    public Trailers() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(trailers);
    }

    public int describeContents() {
        return  0;
    }

}
