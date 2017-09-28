package com.moming.jml.starmovie.entities;

import org.json.JSONArray;

/**
 * Created by jml on 2017/9/28.
 * 电影对象
 */

public class NewMovieEntity {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public JSONArray getType() {
        return type;
    }

    public void setType(JSONArray type) {
        this.type = type;
    }

    public JSONArray getCompany() {
        return company;
    }

    public void setCompany(JSONArray company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    private String id;
    private String title;
    private String img_path;
    private String vote;
    private String overview;
    private JSONArray type;
    private JSONArray company;
    private String status;
    private String release_date;
    private String runtime;
    private String revenue;


}
