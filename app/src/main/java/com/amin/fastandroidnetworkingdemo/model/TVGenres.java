
package com.amin.fastandroidnetworkingdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TVGenres implements Parcelable {

    @SerializedName("genres")
    @Expose
    private List<TVGenre> tvGenres = new ArrayList<>();
    public final static Creator<TVGenres> CREATOR = new Creator<TVGenres>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TVGenres createFromParcel(Parcel in) {
            return new TVGenres(in);
        }

        public TVGenres[] newArray(int size) {
            return (new TVGenres[size]);
        }

    };

    protected TVGenres(Parcel in) {
        in.readList(this.tvGenres, (TVGenre.class.getClassLoader()));
    }

    public TVGenres() {
    }

    public List<TVGenre> getTvGenres() {
        return tvGenres;
    }

    public void setTvGenres(List<TVGenre> tvGenres) {
        this.tvGenres = tvGenres;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(tvGenres);
    }

    public int describeContents() {
        return 0;
    }

}
