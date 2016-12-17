package com.example.materialdesigntest.gsonBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class GMovie {

    public Rating rating;

    public class Rating {
        public int max;
        public float average;
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
    public List<CastsOverview> casts;
    public int collect_count; //看过人数
    public int wish_count;//想看人数
    public int ratings_count;//评分人数
    public String alt;//条目url


}
