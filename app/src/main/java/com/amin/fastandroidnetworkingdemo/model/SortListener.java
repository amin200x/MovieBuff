package com.amin.fastandroidnetworkingdemo.model;

public interface SortListener {
    enum SortType {
        SortByNameAZ,
        SortByNameZA,
        SortByRateAsc,
        SortByRateDesc,
        SortByReleasedDateAsc,
        SortByReleasedDateDesc;
    }

    void sort(SortType sortType);

}
