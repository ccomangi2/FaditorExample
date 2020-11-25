package com.faditor.faditorexample.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class NoticeData {
    String user_name;

    public NoticeData() {
    }
    public NoticeData(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    @Exclude
    public Map<String, Object> getNoticeData() {
        Map<String, Object> docData = new HashMap<>();
        docData.put("user_name", user_name);
        //docData.put("photoUri",photoUri);
        return docData;
    }
}
