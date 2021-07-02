package com.mobiledevteam.filmanuncios.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.CategoryListAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeFeatureProductAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeNearProductAdapter;
import com.mobiledevteam.filmanuncios.home.HomeActivity;
import com.mobiledevteam.filmanuncios.home.OneProductActivity;
import com.mobiledevteam.filmanuncios.inbox.InboxHomeActivity;
import com.mobiledevteam.filmanuncios.model.Category;
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

    ArrayList<Product> mAllFeatureProductList = new ArrayList<>();

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

        mAllFeatureProductList.add(new Product("1","1","Porsche Boxster car", "1234567890","300"));
        mAllFeatureProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","200"));
        mAllFeatureProductList.add(new Product("3","1","Porsche Boxster car", "1234567890","300"));
        mAllFeatureProductList.add(new Product("4","1","Toyota Auris 5233 car", "1234567890","400"));
        mAllFeatureProductList.add(new Product("5","1","Porsche Boxster car", "1234567890","300"));
        mAllFeatureProductList.add(new Product("6","1","Porsche Boxster car", "1234567890","300"));
        mAllFeatureProductList.add(new Product("7","1","Porsche Boxster car", "1234567890","300"));
        initViewFavProduct();
    }

    private void initViewFavProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                HomeNearProductAdapter adapter_product = new HomeNearProductAdapter(getBaseContext(), mAllFeatureProductList);
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