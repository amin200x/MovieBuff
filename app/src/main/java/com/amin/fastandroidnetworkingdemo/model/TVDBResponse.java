package com.amin.fastandroidnetworkingdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TVDBResponse implements Parcelable {

    @SerializedName("results")
    @Expose
    private List<TV> tvs = new ArrayList<>();
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("page")
    @Expose
    private Integer page;
    public final static Parcelable.Creator<TVDBResponse> CREATOR = new Creator<TVDBResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TVDBResponse createFromParcel(Parcel in) {
            return new TVDBResponse(in);
        }

        public TVDBResponse[] newArray(int size) {
            return (new TVDBResponse[size]);
        }

    };


    protected TVDBResponse(Parcel in) {
        in.readList(this.tvs, (TV.class.getClassLoader()));
        this.totalResults = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.page = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public TVDBResponse() {
    }

    public List<TV> getTvs() {
        return tvs;
    }

    public void setTvs(List<TV> tvs) {
        this.tvs = tvs;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(tvs);
        dest.writeValue(totalResults);
        dest.writeValue(totalPages);
        dest.writeValue(page);
    }

    public int describeContents() {
        return 0;
    }

}