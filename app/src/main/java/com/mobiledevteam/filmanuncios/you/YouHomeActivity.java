package com.mobiledevteam.filmanuncios.you;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.favorite.FavHomeActivity;
import com.mobiledevteam.filmanuncios.home.HomeActivity;
import com.mobiledevteam.filmanuncios.inbox.InboxHomeActivity;
import com.mobiledevteam.filmanuncios.model.Product;
import com.mobiledevteam.filmanuncios.upload.UploadHomeActivity;

import java.util.ArrayList;

public class YouHomeActivity extends AppCompatActivity {
    private LinearLayout _menuHome;
    private LinearLayout _menuFav;
    private LinearLayout _menuUpload;
    private LinearLayout _menuInbox;
    private LinearLayout _menuYou;
    private ImageView _imguser;
    private TextView _txtuser;

    private ArrayList<Product> mMyProduct;

    private String user_id;
    private String username;
    private String imgurl;
    private String address;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_home);
        _menuHome= (LinearLayout)findViewById(R.id.menu_home);
        _menuFav= (LinearLayout)findViewById(R.id.menu_fav);
        _menuUpload= (LinearLayout)findViewById(R.id.menu_upload);
        _menuInbox= (LinearLayout)findViewById(R.id.menu_inbox);
        _menuYou= (LinearLayout)findViewById(R.id.menu_you);

        _imguser = (ImageView)findViewById(R.id.img_user);
        _txtuser = (TextView)findViewById(R.id.txt_username);

        user_id= Common.getInstance().getUserID();
        setReady();
        getData();
    }

    private void setReady() {
        _menuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoHome();
            }
        });
        _menuFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoFav();
            }
        });
        _menuUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoUpload();
            }
        });
        _menuInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoInbox();
            }
        });
        _menuYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoYou();
            }
        });
    }
    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("userid", user_id);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getOneUser")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject user_object = result.getAsJsonObject("userInfo");
                                username = user_object.get("username").getAsString();
                                imgurl = user_object.get("photo").getAsString();
                                address = user_object.get("address").getAsString();
                                latitude = user_object.get("latitude").getAsString();
                                longitude = user_object.get("longitude").getAsString();

                                JsonArray product_array = result.get("productsInfo").getAsJsonArray();
                                for(JsonElement productElement : product_array){
                                    JsonObject theOne = productElement.getAsJsonObject();
                                    String id = theOne.get("id").getAsString();
                                    String categoryid = theOne.get("categoryid").getAsString();
                                    String title = theOne.get("title").getAsString();
                                    String price = theOne.get("price").getAsString();
                                    String video = theOne.get("video").getAsString();
                                    String pdate = theOne.get("publicdate").getAsString();
                                    String status = theOne.get("status").getAsString();
                                    LatLng plocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    mMyProduct.add(new Product(id,user_id,categoryid,title,video,price,plocation,pdate,status));
                                }
                                setData();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        _txtuser.setText(username);
        Common.getInstance().setmMyProduct(mMyProduct);
        Glide.with(this).asBitmap().load(Uri.parse(Common.getInstance().getBaseURL() + imgurl)).into(_imguser);
    }


    private void onGoHome(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void onGoFav(){
        Intent intent = new Intent(getApplicationContext(), FavHomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void onGoUpload(){
        Intent intent = new Intent(getApplicationContext(), UploadHomeActivity.class);
        startActivity(intent);
        finish();

    }
    private void onGoInbox(){
        Intent intent = new Intent(getApplicationContext(), InboxHomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void onGoYou(){
//        Intent intent = new Intent(getApplicationContext(), YouHomeActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onBackPressed() {
    }
}