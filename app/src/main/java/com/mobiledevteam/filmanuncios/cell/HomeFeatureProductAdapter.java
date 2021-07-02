package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerView;
import com.koushikdutta.ion.Ion;
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
//        Ion.with(mContext).load(mProduct.get(position).getmImage()).intoImageView(holder.image);
        holder.vimeo_player.initialize(59777392);

        holder.name.setText(mProduct.get(position).getmName());
        holder.price.setText(mProduct.get(position).getmPrice() + "$");

//

        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        VimeoPlayerView vimeo_player;
        TextView price;
        TextView name;
        LinearLayout mainlayout;

        public ViewHolder(View itemView) {
            super(itemView);
//            image = itemView.findViewById(R.id.img_product);
            vimeo_player = itemView.findViewById(R.id.vimeo_product);
            name = itemView.findViewById(R.id.txt_product_title);
            price = itemView.findViewById(R.id.txt_product_price);
            mainlayout = itemView.findViewById(R.id.layout_main);
        }
    }
}