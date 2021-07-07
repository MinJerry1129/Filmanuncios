package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.mobiledevteam.filmanuncios.cell.HomeNearProductAdapter;
import com.mobiledevteam.filmanuncios.model.Product;

import java.util.ArrayList;

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
    private String seluser_id;
    private String loginstatus;

    private String username;
    private String imgurl;
    private String address;
    private String latitude;
    private String longitude;
    ArrayList<Product> mAllProduct = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_user);
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
        seluser_id = Common.getInstance().getSeluserID();
        user_id = Common.getInstance().getUserID();
        loginstatus = Common.getInstance().getLogin_status();

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
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@"+ latitude +","+ longitude+",14z"));
                startActivity(intent);
            }
        });
        _likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLikeProduct();
            }
        });
        if (loginstatus.equals("no")){
            _likeImg.setVisibility(View.GONE);
        }
        _infoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.getInstance().setProduct_id(mAllProduct.get(position).getmId());
                Intent intent = new Intent(getApplicationContext(), OneProductActivity.class);
                startActivity(intent);
                finish();
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
        json.addProperty("userid", seluser_id);

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
                                mAllProduct = new ArrayList<>();
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
                                    mAllProduct.add(new Product(id,seluser_id,categoryid,title,video,price,plocation,pdate,status));
                                }
                                setData();
                                initViewProduct();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViewProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                HomeNearProductAdapter adapter_product = new HomeNearProductAdapter(getBaseContext(), mAllProduct);
                _infoGrid.setAdapter(adapter_product);
            }
        });
    }

    private void setData() {
        _userTxt.setText(username);
        _addressTxt.setText(address);
        _productTxt.setText(String.valueOf(mAllProduct.size()));
        Glide.with(this).asBitmap().load(Uri.parse(Common.getInstance().getBaseURL() + imgurl)).into(_userImg);
    }

    private void setLikeProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Set Like...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("userid", user_id);
        json.addProperty("seluserid", seluser_id);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/setFavUser")
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
}