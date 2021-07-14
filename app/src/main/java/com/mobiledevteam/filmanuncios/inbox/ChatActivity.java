package com.mobiledevteam.filmanuncios.inbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {
    ChatView chatView;
    FirebaseDatabase database;
    DatabaseReference myMsgRef;
    DatabaseReference oppMsgRef;
    TextView opposite_name;
    JsonObject opposite;
    String opposite_type;
    String user_id;
    String client_id;
    ChildEventListener _childListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        user_id = Common.getInstance().getUserID();
        client_id = Common.getInstance().getSeluserID();
        chatView = findViewById(R.id.chat_view);

        database = FirebaseDatabase.getInstance();
        myMsgRef = database.getReference("chat/user/" + user_id + "/" + client_id);
        oppMsgRef = database.getReference("chat/user/"+ client_id + "/" + user_id);
        _childListener = myMsgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage value = dataSnapshot.getValue(ChatMessage.class);
                database.getReference("chat/user/" + user_id + "/" + client_id + "/"+dataSnapshot.getKey() + "/status").setValue("yes");
                if(value!=null)
                    chatView.addMessage(value);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getReady();
    }
    public void getReady(){
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                myMsgRef.push().setValue(chatMessage);
                chatMessage.setType(ChatMessage.Type.RECEIVED);
                oppMsgRef.push().setValue(chatMessage);
                chatView.getInputEditText().setText("");
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        myMsgRef.removeEventListener(_childListener);
        Intent intent = new Intent(getApplicationContext(), InboxHomeActivity.class);
        startActivity(intent);
        finish();
    }
}