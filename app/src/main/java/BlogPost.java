package com.example.ishmaamin.curatech;

import java.util.Date;

/**
 * Created by Ishma Amin on 16-09-2018.
 */



public class BlogPost {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String user_id;
    public String image_url;
    public String desc;
    public String title;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date timestamp;

    public BlogPost(){

    }

    public BlogPost(String user_id, String image_url, String desc, String title,Date timestamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.title = title;
        this.timestamp=timestamp;

    }

    public String getUser_id() {
        return user_id;

    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



}
