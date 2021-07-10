package com.mobiledevteam.filmanuncios;

import android.location.Location;

import com.mobiledevteam.filmanuncios.model.Category;
import com.mobiledevteam.filmanuncios.model.Product;

import java.util.ArrayList;

public class Common {
    private static Common instance = new Common();
//    private String baseURL = "http://todoloanuncio.com/";
    private String baseURL = "http://10.0.2.2/filma/";
    private ArrayList<Category> mCategory;
    private String clinicpagetype;
    private String login_status="no";
    private String userID="no";
    private String imgUrl = "";
    private String upload_category_id = "0";
    private String product_id ;
    private String seluserID;
    private String selcategoryID;
    private ArrayList<Product> mAllProduct;
    private ArrayList<Product> mMyProduct;
    private String minvalue;
    private String maxvalue;
    private String duration;
    private String postdate;
    private Location my_location;

    public void Comon(){
        //this.baseURL="http://localhost/jsontest/";
    }

    public static Common getInstance() {
        return instance;
    }
    public String getBaseURL() {return baseURL;}
    public String getClinicpagetype() { return clinicpagetype;}
    public void setClinicpagetype(String clinicpagetype) {this.clinicpagetype = clinicpagetype;}

    public String getLogin_status() {return login_status;}
    public void setLogin_status(String login_status) {this.login_status = login_status;}

    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<Category> getmCategory() {return mCategory; }
    public void setmCategory(ArrayList<Category> mCategory) { this.mCategory = mCategory;}

    public String getUpload_category_id() {return upload_category_id;}
    public void setUpload_category_id(String upload_category_id) {this.upload_category_id = upload_category_id;}

    public String getUserID() { return userID;}
    public void setUserID(String userID) {this.userID = userID;}

    public String getProduct_id() {return product_id;}
    public void setProduct_id(String product_id) {this.product_id = product_id;}

    public String getSeluserID() { return seluserID;}
    public void setSeluserID(String seluserID) {this.seluserID = seluserID;}

    public ArrayList<Product> getmAllProduct() {return mAllProduct;}
    public void setmAllProduct(ArrayList<Product> mAllProduct) {this.mAllProduct = mAllProduct;}

    public String getSelcategoryID() {return selcategoryID;}
    public void setSelcategoryID(String selcategoryID) {this.selcategoryID = selcategoryID;}

    public String getDuration() {return duration;}
    public void setDuration(String duration) {this.duration = duration;}

    public String getMaxvalue() {return maxvalue;}
    public void setMaxvalue(String maxvalue) {this.maxvalue = maxvalue;}

    public String getMinvalue() { return minvalue;}
    public void setMinvalue(String minvalue) { this.minvalue = minvalue;}

    public Location getMy_location() { return my_location;}
    public void setMy_location(Location my_location) {this.my_location = my_location;}

    public String getPostdate() {return postdate;}
    public void setPostdate(String postdate) {this.postdate = postdate;}

    public ArrayList<Product> getmMyProduct() {return mMyProduct;}
    public void setmMyProduct(ArrayList<Product> mMyProduct) {this.mMyProduct = mMyProduct;}
}
