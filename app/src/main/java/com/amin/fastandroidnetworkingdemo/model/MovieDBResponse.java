package com.amin.fastandroidnetworkingdemo.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDBResponse implements Parcelable
{
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    @SerializedName("results")
    @Expose
    private List<Movie> movies = new ArrayList<>();

    @SerializedName("total_results")
    @Expose
    private Integer totalMovies;

    public final static Parcelable.Creator<MovieDBResponse> CREATOR = new Creator<MovieDBResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieDBResponse createFromParcel(Parcel in) {
            return new MovieDBResponse(in);
        }

        public MovieDBResponse[] newArray(int size) {
            return (new MovieDBResponse[size]);
        }

    }
            ;

    protected MovieDBResponse(Parcel in) {
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.movies, (Movie.class.getClassLoader()));
        this.totalMovies = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public MovieDBResponse() {
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> Movies) {
        this.movies = Movies;
    }

    public Integer getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(Integer totalMovies) {
        this.totalMovies = totalMovies;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(totalPages);
        dest.writeList(movies);
        dest.writeValue(totalMovies);
        dest.writeValue(page);
    }

    public int describeContents() {
        return 0;
    }

}