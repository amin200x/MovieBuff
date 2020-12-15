package com.amin.fastandroidnetworkingdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TV implements Parcelable {

    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = new ArrayList<>();
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<>();
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    public final static Parcelable.Creator<TV> CREATOR = new Creator<TV>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TV createFromParcel(Parcel in) {
            return new TV(in);
        }

        public TV[] newArray(int size) {
            return (new TV[size]);
        }

    };


    protected TV(Parcel in) {
        this.voteAverage = ((Double) in.readValue((Double.class.getClassLoader())));
        this.popularity = ((Double) in.readValue((Double.class.getClassLoader())));
        this.voteCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.originCountry, (java.lang.String.class.getClassLoader()));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.firstAirDate = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.genreIds, (java.lang.Integer.class.getClassLoader()));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalName = ((String) in.readValue((String.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public TV() {
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(voteAverage);
        dest.writeValue(popularity);
        dest.writeValue(voteCount);
        dest.writeList(originCountry);
        dest.writeValue(backdropPath);
        dest.writeValue(firstAirDate);
        dest.writeList(genreIds);
        dest.writeValue(overview);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalName);
        dest.writeValue(posterPath);
        dest.writeValue(name);
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}
