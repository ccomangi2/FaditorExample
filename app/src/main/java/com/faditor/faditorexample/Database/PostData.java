package com.faditor.faditorexample.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PostData {
    int position;
    String userId;  //유저 아이디
    String contents; //내용
    String postDate;    //게시 날짜
    public String photo;
    public String photoName; // 게시글사진 이름(사진삭제할때 필요, 절대경로를 뜻함)
    public String userprofileimage; // 회원가입시 프로필사진
    public String userprofileName;
    public String username; // 회원가입시 닉네임

    public int starCount = 0; // 좋아요 갯수
    public Map<String, Boolean> stars = new HashMap<>(); // 좋아요 한 사람

    public PostData() {}

    public PostData(String userId, String userprofileimage, String username, String postDate, String contents,  String photoName, String photo, String userprofileName) {
        this.userId = userId;
        this.postDate = postDate;
        this.contents = contents;
        this.photo = photo;
        this.photoName = photoName;
        this.userprofileimage = userprofileimage;
        this.username = username;
    }
    public PostData(String userprofileimage, String username, String postDate, String contents,  String photoName, String photo, String userprofileName) {
        this.userprofileName = userprofileName;
        this.position = position;
        this.postDate = postDate;
        this.contents = contents;
        this.photo = photo;
        this.photoName = photoName;
        this.userprofileimage = userprofileimage;
        this.username = username;
    }

    @Exclude
    public Map<String, Object> getPostData(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("userId",userId);
        docData.put("username", username);
        docData.put("userprofileimage", userprofileimage);
        docData.put("photoName", photoName);
        docData.put("photo", photo);
        docData.put("postDate",postDate);
        docData.put("contents",contents);
        docData.put("starCount", starCount);
        docData.put("userprofileName", userprofileName);
        //docData.put("photoUri",photoUri);
        return  docData;
    }

    public String getUserprofileName() {
        return userprofileName;
    }

    public void setUserprofileName(String userprofileName) {
        this.userprofileName = userprofileName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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
