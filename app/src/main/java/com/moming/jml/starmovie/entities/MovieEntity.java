package com.moming.jml.starmovie.entities;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jml on 2017/9/22.
 */

public final class MovieEntity {
    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_rating() {
        return movie_rating;
    }

    public void setMovie_rating(String movie_rating) {
        this.movie_rating = movie_rating;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getMovie_url() {
        return movie_url;
    }

    public void setMovie_url(String movie_url) {
        this.movie_url = movie_url;
    }
    public String getMovie_img_url() {
        return movie_img_url;
    }

    public void setMovie_img_url(String movie_img_url) {
        this.movie_img_url = movie_img_url;
    }
    private String movie_name;//电影名称
    private String movie_rating;//电影评分
    private String movie_id;//电影ID
    private String movie_url;//电影链接
    private String movie_img_url;//电影图片
    private String movie_year;//电影年份
    private String movie_summary;//电影简介
    private JSONArray movie_casts;//电影主演


    public String getMovie_year() {
        return movie_year;
    }

    public void setMovie_year(String movie_year) {
        this.movie_year = movie_year;
    }

    public String getMovie_summary() {
        return movie_summary;
    }

    public void setMovie_summary(String movie_summary) {
        this.movie_summary = movie_summary;
    }

    public JSONArray getMovie_casts() {
        return movie_casts;
    }

    public void setMovie_casts(JSONArray movie_casts) {
        this.movie_casts = movie_casts;
    }


}
