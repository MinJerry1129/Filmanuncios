package com.mobiledevteam.filmanuncios.model;

import com.mobiledevteam.filmanuncios.Common;

public class FavUser {
    private String mId;
    private String mFUserId;
    private String mUsername;
    private String mPhoto;
    public FavUser(String id,String fuserid,String username, String photo){
        mId=id;
        mFUserId = fuserid;
        mUsername = username;
        mPhoto = photo;
    }
    public String getmId() {
        return mId;
    }
    public String getmFUserId() {return mFUserId;}
    public String getmUsername() {return mUsername;}
    public String getmPhoto() {return Common.getInstance().getBaseURL() + mPhoto;}
}