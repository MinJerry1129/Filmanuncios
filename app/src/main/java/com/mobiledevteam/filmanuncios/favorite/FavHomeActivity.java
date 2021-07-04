package com.mobiledevteam.filmanuncios.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.CategoryListAdapter;
import com.mobiledevteam.filmanuncios.cell.FavProductAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeFeatureProductAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeNearProductAdapter;
import com.mobiledevteam.filmanuncios.home.HomeActivity;
import com.mobiledevteam.filmanuncios.home.OneProductActivity;
import com.mobiledevteam.filmanuncios.inbox.InboxHomeActivity;
import com.mobiledevteam.filmanuncios.model.Category;
import com.mobiledevteam.filmanuncios.model.FavProduct;
import com.mobiledevteam.filmanuncios.model.Product;
import com.mobiledevteam.filmanuncios.upload.UploadHomeActivity;
import com.mobiledevteam.filmanuncios.you.YouHomeActivity;

import java.util.ArrayList;

public class FavHomeActivity extends AppCompatActivity {
    private LinearLayout _menuHome;
    private LinearLayout _menuFav;
    private LinearLayout _menuUpload;
    private LinearLayout _menuInbox;
    private LinearLayout _menuYou;

    private GridView _favproductGrid;

    ArrayList<FavProduct> mAllFeatureProductList = new ArrayList<>();

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_home);
        _menuHome= (LinearLayout)findViewById(R.id.menu_home);
        _menuFav= (LinearLayout)findViewById(R.id.menu_fav);
        _menuUpload= (LinearLayout)findViewById(R.id.menu_upload);
        _menuInbox= (LinearLayout)findViewById(R.id.menu_inbox);
        _menuYou= (LinearLayout)findViewById(R.id.menu_you);

        _favproductGrid = (GridView) findViewById(R.id.grid_fav_product);
        userid = Common.getInstance().getUserID();

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
//        _favproductGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), OneProductActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("userid", userid);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getFavProduct")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {

                                JsonArray product_array = result.get("favproduct").getAsJsonArray();
                                for(JsonElement productElement : product_array){
                                    JsonObject theOne = productElement.getAsJsonObject();
                                    String id = theOne.get("id").getAsString();
                                    String productid = theOne.get("productid").getAsString();
                                    String title = theOne.get("title").getAsString();
                                    String price = theOne.get("price").getAsString();
                                    String video = theOne.get("video").getAsString();

                                    mAllFeatureProductList.add(new FavProduct(id,productid,title,video,price));
                                }

//                                JsonArray products_array = result.get("productsInfo").getAsJsonArray();
//
//                                for(JsonElement productElement : products_array){
//                                    JsonObject theproduct = productElement.getAsJsonObject();
//                                    String id = theproduct.get("id").getAsString();
//                                    String brandid = theproduct.get("brandid").getAsString();
//                                    String name = theproduct.get("name").getAsString();
//                                    String price = theproduct.get("price").getAsString();
//                                    String image = theproduct.get("photo").getAsString();
//                                    String description = theproduct.get("information").getAsString();
//                                    if(selLang.equals("ar")){
//                                        name = theproduct.get("namear").getAsString();
//                                        description = theproduct.get("informationar").getAsString();
//                                    }
//                                    mProduct.add(new HomeProduct(id,brandid,name,price,image,description));
//                                }
                                initViewFavProduct();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initViewFavProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                FavProductAdapter adapter_product = new FavProductAdapter(getBaseContext(), mAllFeatureProductList);
                _favproductGrid.setAdapter(adapter_product);
            }
        });
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
        Intent intent = new Intent(getApplicationContext(), YouHomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}