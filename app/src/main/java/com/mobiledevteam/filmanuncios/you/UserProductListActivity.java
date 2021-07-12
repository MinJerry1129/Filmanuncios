package com.mobiledevteam.filmanuncios.you;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.FavProductAdapter;
import com.mobiledevteam.filmanuncios.cell.MyProductAdapter;
import com.mobiledevteam.filmanuncios.home.HomeActivity;
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
        _productGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Common.getInstance().setProduct_id(mAllProduct.get(i).getmId());
                Intent intent=new Intent(getBaseContext(), UserProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
        MyProductAdapter adapter_product = new MyProductAdapter(getBaseContext(), mAllProduct);
        _productGrid.setAdapter(adapter_product);
        initProduct();
    }

    private void initProduct() {
    }
}