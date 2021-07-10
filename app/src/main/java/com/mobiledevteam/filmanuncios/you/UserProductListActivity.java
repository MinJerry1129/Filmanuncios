package com.mobiledevteam.filmanuncios.you;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.FavProductAdapter;
import com.mobiledevteam.filmanuncios.cell.MyProductAdapter;
import com.mobiledevteam.filmanuncios.model.FavProduct;
import com.mobiledevteam.filmanuncios.model.Product;

import java.util.ArrayList;

public class UserProductListActivity extends AppCompatActivity {
    private GridView _productGrid;
    ArrayList<Product> mAllProduct = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_list);
        _productGrid = (GridView)findViewById(R.id.grid_product);
        mAllProduct = Common.getInstance().getmMyProduct();
        setReady();
    }

    private void setReady() {
        MyProductAdapter adapter_product = new MyProductAdapter(getBaseContext(), mAllProduct);
        _productGrid.setAdapter(adapter_product);
        initProduct();
    }

    private void initProduct() {
    }
}