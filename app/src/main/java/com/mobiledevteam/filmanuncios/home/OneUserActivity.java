package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;

public class OneUserActivity extends AppCompatActivity {
    private ImageView _userImg;
    private TextView _userTxt;
    private RatingBar _rating;
    private TextView _rateTxt;
    private TextView _saleTxt;
    private TextView _buyTxt;
    private TextView _shippingTxt;
    private TextView _addressTxt;
    private TextView _seelocationTxt;
    private TextView _productTxt;
    private TextView _reviewTxt;
    private TextView _productTextTxt;
    private TextView _reviewTextTxt;
    private LinearLayout _productLayout;
    private LinearLayout _reviewLayout;
    private LinearLayout _uproductLayout;
    private LinearLayout _ureviewLayout;
    private ImageView _likeImg;
    private GridView _infoGrid;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_user);
        user_id = Common.getInstance().getSeluserID();
        _userImg = (ImageView)findViewById(R.id.img_user);
        _userTxt = (TextView)findViewById(R.id.txt_username);
        _rating = (RatingBar)findViewById(R.id.rating);
        _rateTxt = (TextView)findViewById(R.id.txt_rate_count);
        _saleTxt = (TextView)findViewById(R.id.txt_sales);
        _buyTxt = (TextView)findViewById(R.id.txt_buy);
        _shippingTxt = (TextView)findViewById(R.id.txt_shipping);
        _addressTxt = (TextView)findViewById(R.id.txt_address);
        _seelocationTxt = (TextView) findViewById(R.id.txt_see_map);
        _productTxt = (TextView)findViewById(R.id.txt_product);
        _reviewTxt = (TextView)findViewById(R.id.txt_review);
        _productTextTxt = (TextView)findViewById(R.id.txt_product_text);
        _reviewTextTxt= (TextView)findViewById(R.id.txt_review_text);
        _productLayout = (LinearLayout)findViewById(R.id.layout_product);
        _reviewLayout = (LinearLayout)findViewById(R.id.layout_review);
        _uproductLayout = (LinearLayout)findViewById(R.id.underline_product);
        _ureviewLayout = (LinearLayout)findViewById(R.id.underline_review);
        _likeImg = (ImageView)findViewById(R.id.img_fav);
        _infoGrid = (GridView)findViewById(R.id.grid_info);

        setReady();
        getData();
    }

    private void setReady() {
        _productLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _uproductLayout.setBackgroundColor(getResources().getColor(R.color.major));
                _ureviewLayout.setBackgroundColor(getResources().getColor(R.color.white));
                _productTxt.setTextColor(getResources().getColor(R.color.black));
                _productTextTxt.setTextColor(getResources().getColor(R.color.black));
                _reviewTxt.setTextColor(getResources().getColor(R.color.lightgray));
                _reviewTextTxt.setTextColor(getResources().getColor(R.color.lightgray));
            }
        });
        _reviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _uproductLayout.setBackgroundColor(getResources().getColor(R.color.white));
                _ureviewLayout.setBackgroundColor(getResources().getColor(R.color.major));
                _productTxt.setTextColor(getResources().getColor(R.color.lightgray));
                _productTextTxt.setTextColor(getResources().getColor(R.color.lightgray));
                _reviewTxt.setTextColor(getResources().getColor(R.color.black));
                _reviewTextTxt.setTextColor(getResources().getColor(R.color.black));
            }
        });
        _seelocationTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        _likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getData() {

    }
}