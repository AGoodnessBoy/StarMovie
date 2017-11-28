package com.moming.jml.starmovie.entities;

/**
 * Created by admin on 2017/11/28.
 */

public class VideosEntity {

    private String movie_id;
    private String video_id;
    private String video_key;
    private String video_name;
    private String video_site;
    private String info;


    public String getMovie_id() {
        return movie_id;
    }

    public String getInfo() {
        return info;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getVideo_key() {
        return video_key;
    }

    public String getVideo_name() {
        return video_name;
    }

    public String getVideo_site() {
        return video_site;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public void setVideo_key(String video_key) {
        this.video_key = video_key;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public void setVideo_site(String video_site) {
        this.video_site = video_site;
    }
}
