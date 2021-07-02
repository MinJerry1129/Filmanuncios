package com.mobiledevteam.filmanuncios;

import com.mobiledevteam.filmanuncios.model.Category;

import java.util.ArrayList;

public class Common {
    private static Common instance = new Common();
//    private String baseURL = "http://proderma.online/";
        private String baseURL = "http://10.0.2.2/filma/";
    private ArrayList<Category> mCategory;
    private String clinicpagetype;
    private String login_status="no";
    private String userID;
    private String imgUrl = "";
    private String upload_category_id = "0";

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
}
