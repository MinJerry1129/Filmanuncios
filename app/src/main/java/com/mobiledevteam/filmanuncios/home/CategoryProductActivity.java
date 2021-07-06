package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.CategoryListAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeFeatureProductAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeNearProductAdapter;
import com.mobiledevteam.filmanuncios.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CategoryProductActivity extends AppCompatActivity {
    private LinearLayout _filterLayout, _locationLayout;
    private Spinner _sortSpinner;

    private RecyclerView _featureproductRecycle,_featureusertRecycle;
    private GridView _nearproductGrid;

    ArrayList<Product> mAllFeatureProductList = new ArrayList<>();
    ArrayList<Product> mAllNearProductList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product);
        _sortSpinner = (Spinner) findViewById(R.id.spinner_sort);
        _filterLayout = (LinearLayout)findViewById(R.id.layout_filter);
        _locationLayout = (LinearLayout)findViewById(R.id.layout_location);

        _featureproductRecycle = (RecyclerView)findViewById(R.id.recycler_feature_product);
        _featureusertRecycle = (RecyclerView)findViewById(R.id.recycler_feature_user);
        _nearproductGrid = (GridView) findViewById(R.id.grid_near_product);
        LinearLayoutManager layoutManager_feature_user = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_feature_product = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _featureproductRecycle.setLayoutManager(layoutManager_feature_product);
        _featureusertRecycle.setLayoutManager(layoutManager_feature_user);

        setSortSpineer();
        setReady();
        getData();
    }

    public void setSortSpineer() {
        List<String> list = new ArrayList<String>();
        list.add("Distance");
        list.add("Highest($)");
        list.add("Lowest($)");
        list.add("Newest");
        list.add("Oldest");
        list.add("Title A~Z");
        list.add("Title Z~A");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _sortSpinner.setAdapter(dataAdapter);
    }

    private void setReady() {
        _sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        _filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryFilterActivity.class);
                startActivity(intent);
            }
        });
        _locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationMapActivity.class);
                startActivity(intent);
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
//        mAllFeatureProductList.add(new Product("1","1","Porsche Boxster car", "1234567890","300"));
//        mAllFeatureProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","200"));
//        mAllFeatureProductList.add(new Product("3","1","Porsche Boxster car", "1234567890","300"));
//        mAllFeatureProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","400"));
//        mAllFeatureProductList.add(new Product("4","1","Porsche Boxster car", "1234567890","300"));
//
//
//        mAllNearProductList.add(new Product("1","1","Porsche Boxster car", "1234567890","300"));
//        mAllNearProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","200"));
//        mAllNearProductList.add(new Product("3","1","Porsche Boxster car", "1234567890","300"));
//        mAllNearProductList.add(new Product("2","1","Toyota Auris 5233 car", "1234567890","400"));
//        mAllNearProductList.add(new Product("4","1","Porsche Boxster car", "1234567890","300"));
//        initViewFeatureProduct();
//        initViewNearProduct();
//        initViewFeatureUser();
    }

    private void initViewFeatureUser(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                HomeFeatureProductAdapter adapter_product = new HomeFeatureProductAdapter(getBaseContext(), mAllFeatureProductList);
                _featureusertRecycle.setAdapter(adapter_product);
//                adapter_brand.setOnClick(AllProductActivity.this);
            }
        });
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

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
}