package com.moming.jml.starmovie.entities;

/**
 * Created by admin on 2017/11/28.
 */

public class ReviewsEntity {

    private String movie_id;
    private String reviews_id;
    private String reviews_content;
    private String author;
    private String info;

    public String getAuthor() {
        return author;
    }

    public String getInfo() {
        return info;
    }

    public String getReviews_content() {
        return reviews_content;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getReviews_id() {
        return reviews_id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setReviews_id(String reviews_id) {
        this.reviews_id = reviews_id;
    }

    public void setReviews_content(String reviews_content) {
        this.reviews_content = reviews_content;
    }
}
