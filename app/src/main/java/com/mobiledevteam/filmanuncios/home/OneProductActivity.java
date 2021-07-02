package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mobiledevteam.filmanuncios.R;

public class OneProductActivity extends AppCompatActivity {
    LinearLayout _userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);
        _userLayout = (LinearLayout)findViewById(R.id.layout_user);
        setReady();
    }

    private void setReady() {
        _userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OneUserActivity.class);
                startActivity(intent);
            }
        });
    }
}