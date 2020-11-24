package com.faditor.faditorexample.Database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserData {
    String name; //이름
    String email; //이메일
    String user_name;
    String user_intro;
    String like_fashion;
    String photoUri;

    public UserData() {}

    public UserData(String name, String email, String user_name, String user_intro, String like_fashion, String photoUri) {
        this.name = name;
        this.email = email;
        this.user_name = user_name;
        this.user_intro = user_intro;
        this.like_fashion = like_fashion;
        this.photoUri = photoUri;
    }
    @Exclude
    public Map<String, Object> getUserData(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("name",name);
        docData.put("email", email);
        docData.put("user_name",user_name);
        docData.put("user_intro", user_intro);
        docData.put("like_fashion",like_fashion);
        docData.put("photoUri", photoUri);
        //docData.put("photoUri",photoUri);
        return  docData;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
