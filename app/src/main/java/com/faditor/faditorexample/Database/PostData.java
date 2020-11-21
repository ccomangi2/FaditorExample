package com.faditor.faditorexample.Database;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostData implements Serializable {
    String userId;  //유저 아이디
    Date postDate;    //게시 날짜
    String contents;    //게시글 내용
    String photoUri;    //사진 경로

    public PostData(String userId, Date postDate, String contents, String photoUri) {
        this.userId = userId;
        this.postDate = postDate;
        this.contents = contents;
        this.photoUri = photoUri;
    }
    public PostData(Date postDate, String contents, String photoUri) {
        this.postDate = postDate;
        this.contents = contents;
        this.photoUri = photoUri;
    }
    public Map<String, Object> getPostData(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("postDate",postDate);
        docData.put("contents",contents);
        docData.put("photoUri",photoUri);
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

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
