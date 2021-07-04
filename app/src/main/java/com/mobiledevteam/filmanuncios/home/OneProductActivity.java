package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;

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
    private String product_latitude;
    private String product_longitude;
    private String product_address;

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