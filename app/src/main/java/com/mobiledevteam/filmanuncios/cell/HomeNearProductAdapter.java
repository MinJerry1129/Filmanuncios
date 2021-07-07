package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
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

//        VideoView _productVideo = (VideoView)listItem.findViewById(R.id.video_product);
//        _productVideo.setVideoURI(Uri.parse(currentProduct.getmVideoID()));
//        _productVideo.start();
//        _productVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setLooping(true);
//            }
//        });
//
        ImageView product_image = (ImageView) listItem.findViewById(R.id.img_product);
        Glide.with(mContext).asBitmap().load(Uri.parse(Common.getInstance().getBaseURL() + currentProduct.getmVideoID())).into(product_image);
//        Ion.with(mContext).load(currentProduct.getmImage()).intoImageView(product_image);
        TextView name = (TextView) listItem.findViewById(R.id.txt_product_title);
        name.setText(currentProduct.getmName());
        TextView price = (TextView) listItem.findViewById(R.id.txt_product_price);
        price.setText(currentProduct.getmPrice() + " $");
//        listItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Common.getInstance().setProduct_id(currentProduct.getmId());
//                Intent intent = new Intent(mContext, OneProductActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);
//            }
//        });
        return listItem;
    }
}