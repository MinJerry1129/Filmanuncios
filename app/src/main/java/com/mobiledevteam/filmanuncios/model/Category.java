package com.mobiledevteam.filmanuncios.model;

public class Category {
    private String mId;
    private String mName;
    private int mImgurl;

    public Category(String id, String name,int imgurl){
        mId=id;
        mName=name;
        mImgurl = imgurl;
    }
    public String getmId() {
        return mId;
    }
    public String getmName() {
        return mName;
    }
    public int getmImgurl() {return mImgurl; }
}
