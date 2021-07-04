package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.model.FavProduct;
import com.mobiledevteam.filmanuncios.model.FavUser;

import java.util.ArrayList;

public class OneProductActivity extends AppCompatActivity {
    private LinearLayout _userLayout;
    private VideoView _productVideo;
    private TextView _priceTxt;
    private TextView _titleTxt;
    private TextView _descriptionTxt;
    private TextView _dateTxt;
    private TextView _eyeTxt;
    private TextView _likeTxt;
    private TextView _locationTxt;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);
        _userLayout = (LinearLayout)findViewById(R.id.layout_user);
        product_id = Common.getInstance().getProduct_id();
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

        _priceTxt.setText(product_price);
        _titleTxt.setText(product_title);
        _descriptionTxt.setText(product_description);
        _dateTxt.setText(product_updatedate);
        _locationTxt.setText(product_address);

    }

    private void setReady() {
        _userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OneUserActivity.class);
                startActivity(intent);
            }
        });
    }
}