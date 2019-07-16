package com.example.ishmaamin.curatech;


public class PresPresBlogPost extends PresBlogPostId {
    public String title,user,desc;


    public PresPresBlogPost(){ }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public PresPresBlogPost(String title, String user, String desc, int time) {
        this.title = title;
        this.user = user;
        this.desc = desc;
    }


}
