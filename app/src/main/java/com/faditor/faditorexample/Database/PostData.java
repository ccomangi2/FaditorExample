package com.faditor.faditorexample.Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostData implements Serializable {
    String userId;  //유저 아이디
    Date postDate;    //게시 날짜
    private ArrayList<String> contents;
    private ArrayList<String> formats;
    public int photo; // 게시글 사진
    public String photoName; // 게시글사진 이름(사진삭제할때 필요, 절대경로를 뜻함)
    public String userprofileimage; // 회원가입시 프로필사진
    public String username; // 회원가입시 닉네임

    public int starCount = 0; // 좋아요 갯수
    public Map<String, Boolean> stars = new HashMap<>(); // 좋아요 한 사람

    public PostData(String userId, Date postDate, ArrayList<String> contents, ArrayList<String> formats, String photoName, String userprofileimage, String username) {
        this.userId = userId;
        this.postDate = postDate;
        this.contents = contents;
        this.formats = formats;
        this.photo = photo;
        this.photoName = photoName;
        this.userprofileimage = userprofileimage;
        this.username = username;
    }
    public PostData(Date postDate, ArrayList<String> contents, ArrayList<String> formats) {
        this.postDate = postDate;
        this.contents = contents;
        this.formats = formats;
    }
    public Map<String, Object> getPostData(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("userId",userId);
        docData.put("username", username);
        docData.put("userprofileimage", userprofileimage);
        docData.put("photoName", photoName);
        docData.put("photo", photo);
        docData.put("postDate",postDate);
        docData.put("contents",contents);
        docData.put("formats", formats);
        docData.put("starCount", starCount);
        //docData.put("photoUri",photoUri);
        return  docData;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public ArrayList<String> getFormats() {
        return formats;
    }

    public void setFormats(ArrayList<String> formats) {
        this.formats = formats;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getUserprofileimage() {
        return userprofileimage;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setUserprofileimage(String userprofileimage) {
        this.userprofileimage = userprofileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }
}
