package com.mobiledevteam.filmanuncios.model;

public class User {
    private String mId;
    private String mUsername;
    private String mPhoto;
    public User(String id,String username, String photo){
        mId=id;
        mUsername = username;
        mPhoto = photo;
    }
    public String getmId() {
        return mId;
    }
    public String getmUsername() {return mUsername;}
    public String getmPhoto() {return mPhoto;}
}
