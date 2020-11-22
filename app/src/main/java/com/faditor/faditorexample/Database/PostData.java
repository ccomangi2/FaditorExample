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
    //String photoUri;    //사진 경로

    public PostData(String userId, Date postDate, ArrayList<String> contents, ArrayList<String> formats) {
        this.userId = userId;
        this.postDate = postDate;
        this.contents = contents;
        this.formats = formats;
    }
    public PostData(Date postDate, ArrayList<String> contents, ArrayList<String> formats) {
        this.postDate = postDate;
        this.contents = contents;
        this.formats = formats;
    }
    public Map<String, Object> getPostData(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("postDate",postDate);
        docData.put("contents",contents);
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
}
