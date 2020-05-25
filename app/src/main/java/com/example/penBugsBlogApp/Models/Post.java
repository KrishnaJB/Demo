package com.example.penBugsBlogApp.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String userId;
    private Object timeStamp;


    public Post(String title, String description, String picture, String userId) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post(){

    }

    //Getter - getTitle

    public String getPostKey() {
        return postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    // Setters - setTitle


    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
