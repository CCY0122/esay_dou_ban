package com.example.materialdesigntest.gsonBean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class GMovie {

    public Rating rating;

    public class Rating {
        public int max;
        public double avergae;
        public String stars;
        public int min;
    }

    public String year;
    public MovieImage images;

    public class MovieImage {
        public String large;
        public String medium;
        public String small;
    }

    public String id;
    public String title;
    public List<String> genres;
    public String summary;
    public List<Casts> casts;


}
