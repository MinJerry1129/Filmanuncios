package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.model.Category;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private ArrayList<Category> mCategory;
    private Context mContext;

    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }
    public CategoryListAdapter(Context context, ArrayList<Category> data) {
        mContext = context;
        mCategory = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(mCategory.get(position).getmName());
        holder.imgCategory.setImageResource(mCategory.get(position).getmImgurl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imgCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_category);
            imgCategory = itemView.findViewById(R.id.img_category);
        }
    }
}