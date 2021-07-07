package com.mobiledevteam.filmanuncios.model;

import com.google.android.gms.maps.model.LatLng;
import com.mobiledevteam.filmanuncios.Common;

public class Product {
    private String mId;
    private String mCompanyId;
    private String mCategoryID;
    private String mName;
    private String mVideoID;
    private String mPrice;
    private LatLng mLocation;
    private String mPDate;
    private String mStatus;

    public Product(String id,String companyid,String categoryid, String name,String videoid, String price, LatLng location,  String pdate, String status){
        mId=id;
        mCompanyId = companyid;
        mCategoryID = categoryid;
        mName=name;
        mVideoID = videoid;
        mPrice = price;
        mPDate = pdate;
        mLocation = location;
        mStatus = status;
    }
    public String getmId() {
        return mId;
    }
    public String getmCompanyId() {return mCompanyId;}
    public String getmCategoryID() { return mCategoryID;}
    public String getmName() {
        return mName;
    }
    public String getmVideoID() {return mVideoID;}
    public String getmPrice() {return mPrice; }
    public LatLng getmLocation() {return mLocation;}
    public String getmPDate() {return mPDate;}
    public String getmStatus() {return mStatus;}

    public void setmId(String mId) { this.mId = mId;}
    public void setmCompanyId(String mCompanyId) {this.mCompanyId = mCompanyId;}
    public void setmCategoryID(String mCategoryID) {this.mCategoryID = mCategoryID;}
    public void setmName(String mName) {this.mName = mName;}
    public void setmVideoID(String mVideoID) {this.mVideoID = mVideoID;}
    public void setmPrice(String mPrice) {this.mPrice = mPrice;}
    public void setmLocation(LatLng mLocation) {this.mLocation = mLocation;}
    public void setmPDate(String mPDate) {this.mPDate = mPDate;}
    public void setmStatus(String mStatus) {this.mStatus = mStatus;}
}
