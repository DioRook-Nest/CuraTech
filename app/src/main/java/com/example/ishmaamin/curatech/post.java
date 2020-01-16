package com.example.ishmaamin.curatech;;

import java.security.Timestamp;
import java.util.Date;


public class post {

    public String image_url;
    public Date timestamp;





    public post(){}


    public post(String image_url,Date timestamp) {
        this.image_url = image_url;
        this.timestamp = timestamp;
    }

    public String getImage_url() {

        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public Date getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }




}
