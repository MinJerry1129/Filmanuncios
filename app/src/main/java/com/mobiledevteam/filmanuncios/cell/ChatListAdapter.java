package com.mobiledevteam.filmanuncios.cell;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.model.User;

import java.util.ArrayList;
import java.util.List;

import co.intentservice.chatui.models.ChatMessage;

public class ChatListAdapter extends ArrayAdapter<User> {
    FirebaseDatabase database;
    DatabaseReference myMsgRef;
    DatabaseReference oppMsgRef;
    String user_id;
    String seluser_id;
    private Context mContext;
    private List<User> allUserList = new ArrayList<>();

    public ChatListAdapter(Context context, ArrayList<User> list) {
        super(context, 0 , list);
        mContext = context;
        allUserList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_chatlist,parent,false);

        user_id = Common.getInstance().getUserID();
        User currentMovie = allUserList.get(position);
        seluser_id = currentMovie.getmId();

        database = FirebaseDatabase.getInstance();
        myMsgRef = database.getReference("chat/user/" + user_id + "/" + seluser_id);

        final TextView _chat_message = (TextView) listItem.findViewById(R.id.chat_count);
        _chat_message.setVisibility(View.INVISIBLE);

        myMsgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int chat_count = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    ChatMessage value = child.getValue(ChatMessage.class);
                    if(value.getStatus().equals("no")){
                        chat_count = chat_count + 1;
                    }
                    if(chat_count == 0){
                        _chat_message.setVisibility(View.INVISIBLE);
                    }else{
                        _chat_message.setVisibility(View.VISIBLE);
                    }
                    Log.d("tag", String.valueOf(chat_count));
                    _chat_message.setText(String.valueOf(chat_count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView user_image = (ImageView) listItem.findViewById(R.id.img_user);
        Glide.with(mContext).asBitmap().load(Uri.parse(Common.getInstance().getBaseURL() + currentMovie.getmPhoto())).into(user_image);

        TextView name = (TextView) listItem.findViewById(R.id.username);
        name.setText(currentMovie.getmUsername());
        return listItem;
    }
}
