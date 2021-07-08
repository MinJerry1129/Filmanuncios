package com.mobiledevteam.filmanuncios.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.model.FavProduct;
import com.mobiledevteam.filmanuncios.model.FavUser;
import com.mobiledevteam.filmanuncios.model.Product;

import java.io.IOException;
import java.util.ArrayList;

public class OneProductActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LinearLayout _userLayout;
    private VideoView _productVideo;
    private TextView _priceTxt;
    private TextView _titleTxt;
    private TextView _descriptionTxt;
    private TextView _dateTxt;
    private TextView _eyeTxt;
    private TextView _likeTxt;
    private TextView _locationTxt;
    private ImageView _likeImg;
    private ImageView _imageView;
    private Button _chatBtn;


    private SupportMapFragment mapFragment;

    private String product_id;
    private String product_title;
    private String product_price;
    private String product_description;
    private String product_eye;
    private String product_like;
    private String product_video;
    private String product_latitude;
    private String product_longitude;
    private String product_address;
    private String product_updatedate;
    private String seluser_id;
    private String userID;
    private String login_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);
        _userLayout = (LinearLayout)findViewById(R.id.layout_user);
        _productVideo = (VideoView)findViewById(R.id.video_product);
        _priceTxt = (TextView)findViewById(R.id.txt_product_price);
        _titleTxt = (TextView)findViewById(R.id.txt_product_name);
        _descriptionTxt = (TextView)findViewById(R.id.txt_product_description);
        _dateTxt = (TextView)findViewById(R.id.txt_updatedate);
        _eyeTxt = (TextView)findViewById(R.id.txt_eye);
        _likeTxt = (TextView)findViewById(R.id.txt_like);
        _locationTxt = (TextView)findViewById(R.id.txt_location);
        _likeImg = (ImageView)findViewById(R.id.img_like);
        _chatBtn = (Button)findViewById(R.id.btn_chat);
        _imageView = (ImageView)findViewById(R.id.img_topbanner);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Glide.with(getBaseContext())
                .load(R.drawable.animation)
                .into(_imageView);
        product_id = Common.getInstance().getProduct_id();
        userID = Common.getInstance().getUserID();
        login_status = Common.getInstance().getLogin_status();
        setReady();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("productid", product_id);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getOneProduct")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject product_object = result.getAsJsonObject("productInfo");

                                product_video = product_object.get("video").getAsString();
                                product_price = product_object.get("price").getAsString();
                                product_title = product_object.get("title").getAsString();
                                product_description = product_object.get("information").getAsString();
                                product_updatedate = product_object.get("publicdate").getAsString();
                                product_latitude = product_object.get("latitude").getAsString();
                                product_longitude = product_object.get("longitude").getAsString();
                                product_address = product_object.get("address").getAsString();
                                seluser_id = product_object.get("userid").getAsString();

                                product_eye = result.get("productEye").getAsString();
                                product_like = result.get("productlike").getAsString();
                                setData();
                                mapFragment.getMapAsync(OneProductActivity.this::onMapReady);
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        _productVideo.setVideoURI(Uri.parse(Common.getInstance().getBaseURL() + product_video));
        _productVideo.start();
        _productVideo.requestFocus();
        _productVideo.setKeepScreenOn(true);
        _productVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        _likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLikeProduct();
            }
        });

        _priceTxt.setText(product_price + " $");
        _titleTxt.setText(product_title);
        _descriptionTxt.setText(product_description);
        _dateTxt.setText(product_updatedate);
        _locationTxt.setText(product_address);

        _eyeTxt.setText(product_eye);
        _likeTxt.setText(product_like);
    }

    private void setLikeProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Set Like...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("userid", userID);
        json.addProperty("productid", product_id);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/setFavProduct")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                _likeImg.setImageDrawable(getResources().getDrawable(R.drawable.redfav));
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng company_location = new LatLng(Double.parseDouble(product_latitude), Double.parseDouble(product_longitude));
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(company_location));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(company_location,10));
    }

    private void setReady() {
        _userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Common.getInstance().setSeluserID(seluser_id);
                Intent intent = new Intent(getApplicationContext(), OneUserActivity.class);
                startActivity(intent);
            }
        });
        if(login_status.equals("no")){
            _likeImg.setVisibility(View.GONE);
            _chatBtn.setVisibility(View.GONE);
        }
    }
}