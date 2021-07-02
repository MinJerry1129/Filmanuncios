package com.mobiledevteam.filmanuncios.home;

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

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.CategoryListAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeFeatureProductAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeNearProductAdapter;
import com.mobiledevteam.filmanuncios.favorite.FavHomeActivity;
import com.mobiledevteam.filmanuncios.inbox.InboxHomeActivity;
import com.mobiledevteam.filmanuncios.login.LoginActivity;
import com.mobiledevteam.filmanuncios.model.Category;
import com.mobiledevteam.filmanuncios.model.Product;
import com.mobiledevteam.filmanuncios.upload.UploadHomeActivity;
import com.mobiledevteam.filmanuncios.you.YouHomeActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements CategoryListAdapter.OnItemClicked{
    private LinearLayout _menuHome;
    private LinearLayout _menuFav;
    private LinearLayout _menuUpload;
    private LinearLayout _menuInbox;
    private LinearLayout _menuYou;
    private RecyclerView _categoryReycle;
    private RecyclerView _featureproductRecycle;
    private GridView _nearproductGrid;
    ArrayList<Category> mAllCategoryList = new ArrayList<>();
    ArrayList<Product> mAllFeatureProductList = new ArrayList<>();
    ArrayList<Product> mAllNearProductList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        _menuHome= (LinearLayout)findViewById(R.id.menu_home);
        _menuFav= (LinearLayout)findViewById(R.id.menu_fav);
        _menuUpload= (LinearLayout)findViewById(R.id.menu_upload);
        _menuInbox= (LinearLayout)findViewById(R.id.menu_inbox);
        _menuYou= (LinearLayout)findViewById(R.id.menu_you);

        _categoryReycle = (RecyclerView)findViewById(R.id.recycler_category);
        _featureproductRecycle = (RecyclerView)findViewById(R.id.recycler_feature_product);
        _nearproductGrid = (GridView) findViewById(R.id.grid_near_product);
        LinearLayoutManager layoutManager_category = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_feature_product = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _categoryReycle.setLayoutManager(layoutManager_category);
        _featureproductRecycle.setLayoutManager(layoutManager_feature_product);
        mAllCategoryList = Common.getInstance().getmCategory();
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
//        _nearproductGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        mAllFeatureProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","400"));
        mAllFeatureProductList.add(new Product("4","1","Porsche Boxster car", "1234567890","300"));


        mAllNearProductList.add(new Product("1","1","Porsche Boxster car", "1234567890","300"));
        mAllNearProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","200"));
        mAllNearProductList.add(new Product("3","1","Porsche Boxster car", "1234567890","300"));
        mAllNearProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","400"));
        mAllNearProductList.add(new Product("4","1","Porsche Boxster car", "1234567890","300"));
        initViewCategory();
        initViewFeatureProduct();
        initViewNearProduct();
    }

    private void initViewFeatureProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                HomeFeatureProductAdapter adapter_product = new HomeFeatureProductAdapter(getBaseContext(), mAllFeatureProductList);
                _featureproductRecycle.setAdapter(adapter_product);
//                adapter_brand.setOnClick(AllProductActivity.this);
            }
        });
    }
    private void initViewNearProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                int count_product = mAllNearProductList.size()/2;
                if(mAllNearProductList.size() % 2 == 1){
                    count_product = count_product + 1;
                }
                ViewGroup.LayoutParams layoutParams = _nearproductGrid.getLayoutParams();
                layoutParams.height = convertDpToPixels(225,getBaseContext()) * count_product; //this is in pixels
                _nearproductGrid.setLayoutParams(layoutParams);

                HomeNearProductAdapter adapter_product = new HomeNearProductAdapter(getBaseContext(), mAllNearProductList);
                _nearproductGrid.setAdapter(adapter_product);
            }
        });
    }

    private void initViewCategory(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                CategoryListAdapter adapter_category = new CategoryListAdapter(getBaseContext(), mAllCategoryList);
                _categoryReycle.setAdapter(adapter_category);
                adapter_category.setOnClick(HomeActivity.this);
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), CategoryProductActivity.class);
        startActivity(intent);
        Log.d("position", String.valueOf(position));
    }


    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    private void onGoHome(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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