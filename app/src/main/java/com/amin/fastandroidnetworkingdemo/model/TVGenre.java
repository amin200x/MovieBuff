
package com.amin.fastandroidnetworkingdemo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVGenre implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    public final static Creator<TVGenre> CREATOR = new Creator<TVGenre>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TVGenre createFromParcel(Parcel in) {
            return new TVGenre(in);
        }

        public TVGenre[] newArray(int size) {
            return (new TVGenre[size]);
        }

    }
    ;

    protected TVGenre(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TVGenre() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
    }

    public int describeContents() {
        return  0;
    }

}
