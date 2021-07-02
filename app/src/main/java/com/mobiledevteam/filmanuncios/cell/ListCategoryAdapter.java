package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.model.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryAdapter extends ArrayAdapter<Category> {
    private Context mContext;
    private ArrayList<Category> allCategory = new ArrayList<>();
    public ListCategoryAdapter(@NonNull Context context, ArrayList<Category> list) {
        super(context, 0, list);
        mContext = context;
        allCategory = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater inflater=context.getLayoutInflater();
        View listItem = convertView;
        Category oneCategory = allCategory.get(position);
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_category_list,parent,false);

        ImageView imageView = (ImageView) listItem.findViewById(R.id.img_category);
        TextView titleText = (TextView) listItem.findViewById(R.id.txt_category);

        titleText.setText(oneCategory.getmName());
        imageView.setImageResource(oneCategory.getmImgurl());

        return listItem;
    }
}
