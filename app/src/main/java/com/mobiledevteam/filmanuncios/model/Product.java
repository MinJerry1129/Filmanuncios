package com.mobiledevteam.filmanuncios.model;

public class Product {
    private String mId;
    private String mCompanyId;
    private String mName;
    private String mVideoID;
    private String mPrice;

    public Product(String id,String companyid, String name,String videoid, String price){
        mId=id;
        mCompanyId = companyid;
        mName=name;
        mVideoID = videoid;
        mPrice = price;
    }
    public String getmId() {
        return mId;
    }
    public String getmCompanyId() {return mCompanyId;}
    public String getmName() {
        return mName;
    }
    public String getmVideoID() {return mVideoID;}
    public String getmPrice() {return mPrice; }
}
