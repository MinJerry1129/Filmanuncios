package com.mobiledevteam.filmanuncios.model;

import com.mobiledevteam.filmanuncios.Common;

public class FavProduct {
    private String mId;
    private String mProductID;
    private String mName;
    private String mVideoID;
    private String mPrice;

    public FavProduct(String id,String productid, String name,String videoid, String price){
        mId=id;
        mProductID = productid;
        mName=name;
        mVideoID = videoid;
        mPrice = price;
    }
    public String getmId() {
        return mId;
    }
    public String getmProductID() {return mProductID;}
    public String getmName() {
        return mName;
    }
    public String getmVideoID() {return Common.getInstance().getBaseURL() +mVideoID;}
    public String getmPrice() {return mPrice; }
}
