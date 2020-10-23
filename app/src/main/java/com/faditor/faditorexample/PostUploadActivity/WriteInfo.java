package com.faditor.faditorexample.PostUploadActivity;

public class WriteInfo {
    private String contents;
    private String userID; // 업로드 한 사람의 ID

    public WriteInfo(String contents, String userID){
        this.contents = contents;
        this.userID = userID;
    }

    public String getContents(){
        return this.contents;
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    public String getUserID(){
        return this.userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
}
