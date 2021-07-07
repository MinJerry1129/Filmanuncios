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
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.home.OneProductActivity;
import com.mobiledevteam.filmanuncios.home.OneUserActivity;
import com.mobiledevteam.filmanuncios.model.FavProduct;
import com.mobiledevteam.filmanuncios.model.FavUser;

import java.util.ArrayList;
import java.util.List;


public class FavUserAdapter extends ArrayAdapter<FavUser> {
    private Context mContext;
    private List<FavUser> allUserList = new ArrayList<>();

    public FavUserAdapter(Context context, ArrayList<FavUser> list) {
        super(context, 0 , list);
        mContext = context;
        allUserList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_fav_user,parent,false);
        FavUser currentUser = allUserList.get(position);//
        ImageView product_image = (ImageView) listItem.findViewById(R.id.img_user);
        Ion.with(mContext).load(currentUser.getmPhoto()).intoImageView(product_image);
        TextView name = (TextView) listItem.findViewById(R.id.txt_username);
        name.setText(currentUser.getmUsername());
        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.getInstance().setSeluserID(currentUser.getmFUserId());
                Intent intent = new Intent(mContext, OneUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return listItem;
    }
}