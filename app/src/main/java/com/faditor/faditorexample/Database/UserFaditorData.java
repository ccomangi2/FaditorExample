package com.faditor.faditorexample.Database;

public class UserFaditorData {
    String user_name;
    String user_intro;
    String like_fashion;
    String photoUri;

    public UserFaditorData() {}

    public UserFaditorData(String user_name, String user_intro, String like_fashion, String photoUri) {
        this.user_name = user_name;
        this.user_intro = user_intro;
        this.like_fashion = like_fashion;
        this.photoUri = photoUri;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_intro() {
        return user_intro;
    }

    public void setUser_intro(String user_intro) {
        this.user_intro = user_intro;
    }

    public String getLike_fashion() {
        return like_fashion;
    }

    public void setLike_fashion(String like_fashion) {
        this.like_fashion = like_fashion;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
