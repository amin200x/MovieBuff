
package com.amin.fastandroidnetworkingdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieGenres implements Parcelable {

    @SerializedName("genres")
    @Expose
    private List<MovieGenre> movieGenres = null;
    public final static Creator<MovieGenres> CREATOR = new Creator<MovieGenres>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieGenres createFromParcel(Parcel in) {
            return new MovieGenres(in);
        }

        public MovieGenres[] newArray(int size) {
            return (new MovieGenres[size]);
        }

    };

    protected MovieGenres(Parcel in) {
        in.readList(this.movieGenres, (MovieGenre.class.getClassLoader()));
    }

    public MovieGenres() {
    }

    public List<MovieGenre> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(List<MovieGenre> movieGenres) {
        this.movieGenres = movieGenres;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(movieGenres);
    }

    public int describeContents() {
        return 0;
    }

}
