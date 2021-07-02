package com.mobiledevteam.filmanuncios.upload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mobiledevteam.filmanuncios.R;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.util.List;

public class UploadProductActivity extends AppCompatActivity {
    private LinearLayout _videoLayout;
    private VideoView _productVideo;
    private TextView _selvideoTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        _videoLayout = (LinearLayout) findViewById(R.id.layout_video);
        _productVideo = (VideoView) findViewById(R.id.video_product);
        _selvideoTxt = (TextView) findViewById(R.id.txt_selvideo);
        setReady();
    }

    private void setReady() {
        _selvideoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VideoPicker.Builder(UploadProductActivity.this)
                        .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            //Your Code
            _productVideo.setVideoPath(mPaths.get(0));
            _productVideo.start();
            _productVideo.setVisibility(View.VISIBLE);
        }
    }
}