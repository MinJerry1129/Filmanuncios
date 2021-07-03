package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.home.OneProductActivity;
import com.mobiledevteam.filmanuncios.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeNearProductAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private List<Product> allProductList = new ArrayList<>();

    public HomeNearProductAdapter(Context context, ArrayList<Product> list) {
        super(context, 0 , list);
        mContext = context;
        allProductList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_near_product,parent,false);
        Product currentProduct = allProductList.get(position);
        VimeoPlayerView vimeo_player = listItem.findViewById(R.id.vimeo_product);
        vimeo_player.initialize(59777392);
//
//        ImageView product_image = (ImageView) listItem.findViewById(R.id.img_product);
//        Ion.with(mContext).load(currentProduct.getmImage()).intoImageView(product_image);
//        TextView name = (TextView) listItem.findViewById(R.id.name_product);
//        name.setText(currentProduct.getmName());
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OneProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return listItem;
    }
}