package com.mobiledevteam.filmanuncios.you;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mobiledevteam.filmanuncios.R;

public class SetBadgeActivity extends AppCompatActivity {
    private TextView _7dayTxt;
    private TextView _15dayTxt;
    private TextView _30dayTxt;
    private TextView _payTxt;
    private TextView _cancelTxt;
    String price = "7.99";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_badge);
        _7dayTxt = (TextView)findViewById(R.id.txt_7day);
        _15dayTxt = (TextView)findViewById(R.id.txt_15day);
        _30dayTxt = (TextView)findViewById(R.id.txt_30day);
        _payTxt = (TextView)findViewById(R.id.txt_pay);
        _cancelTxt = (TextView)findViewById(R.id.txt_cancel);
        setReady();
    }

    private void setReady() {

        _cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}