package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.home.OneProductActivity;
import com.mobiledevteam.filmanuncios.model.Product;

import java.util.ArrayList;

public class HomeFeatureProductAdapter extends RecyclerView.Adapter<HomeFeatureProductAdapter.ViewHolder> {
    private ArrayList<Product> mProduct;
    private Context mContext;

    public HomeFeatureProductAdapter(Context context, ArrayList<Product> data) {
        mContext = context;
        mProduct = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_feature_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(mContext).asBitmap().load(Uri.parse(Common.getInstance().getBaseURL() + mProduct.get(position).getmVideoID())).into(holder.image_product);

        holder.name.setText(mProduct.get(position).getmName());
        holder.price.setText(mProduct.get(position).getmPrice() + "$");

//

        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.getInstance().setProduct_id(mProduct.get(position).getmId());
                Intent intent=new Intent(mContext, OneProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mProduct.size() > 10) {
            return 10;
        } else {
            return mProduct.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView image;
        ImageView image_product;
        TextView price;
        TextView name;
        LinearLayout mainlayout;

        public ViewHolder(View itemView) {
            super(itemView);
//            image = itemView.findViewById(R.id.img_product);
            image_product = itemView.findViewById(R.id.img_product);
            name = itemView.findViewById(R.id.txt_product_title);
            price = itemView.findViewById(R.id.txt_product_price);
            mainlayout = itemView.findViewById(R.id.layout_main);
        }
    }
}