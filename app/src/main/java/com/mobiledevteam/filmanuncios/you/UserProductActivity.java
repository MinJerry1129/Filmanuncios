package com.mobiledevteam.filmanuncios.you;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.home.OneProductActivity;
import com.mobiledevteam.filmanuncios.upload.UploadProductActivity;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class UserProductActivity extends AppCompatActivity {
    private ImageView _badgeImg;
    private VideoView _videoview;
    private TextView _selvideoTxt;
    private EditText _titleTxt;
    private EditText _priceTxt;
    private EditText _descriptionTxt;
    private Button _updateBtn;
    private Button _badgeBtn;

    private String videourl;
    private String title;
    private String price;
    private String description;
    private String status;
    private int durationMs;


    private String product_id;
    private String user_id;
    private String sel_video = "no";
    private List<String> mPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product);
        _videoview = (VideoView)findViewById(R.id.video_product);
        _selvideoTxt = (TextView) findViewById(R.id.txt_selvideo);
        _titleTxt = (EditText)findViewById(R.id.input_title);
        _priceTxt = (EditText)findViewById(R.id.input_price);
        _descriptionTxt = (EditText)findViewById(R.id.input_description);
        _updateBtn= (Button) findViewById(R.id.btn_update);
        _badgeBtn= (Button) findViewById(R.id.btn_badge);
        _badgeImg = (ImageView)findViewById(R.id.img_badge);

        user_id = Common.getInstance().getUserID();
        product_id = Common.getInstance().getProduct_id();
        setReady();
        getData();
    }

    private void setReady() {
        _selvideoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VideoPicker.Builder(UserProductActivity.this)
                        .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                        .directory(VideoPicker.Directory.DEFAULT)
                        .extension(VideoPicker.Extension.MP4)
                        .enableDebuggingMode(true)
                        .build();
            }
        });
        _updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProduct();
            }
        });
        _badgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void uploadProduct() {
        title = _titleTxt.getText().toString();
        price = _priceTxt.getText().toString();
        description = _descriptionTxt.getText().toString();
        if(!validate())
        {
            return;
        }
        String base64 = "";

        if(sel_video.equals("yes")){
            File file = new File(mPaths.get(0));
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = 0;
            try {
                length = new FileInputStream(file).read(buffer);
                base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
                Log.d("base64:", base64);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Update Product...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();
        json.addProperty("productid",product_id);
        json.addProperty("title",title);
        json.addProperty("price",price);
        json.addProperty("description",description);
        json.addProperty("selvideo",sel_video);
        json.addProperty("video", base64);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/updateproduct")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String status = result.get("status").getAsString();
                                if (status.equals("ok")) {
//                                    signup_status = "yes";
                                    Toast.makeText(getBaseContext(),"Update Product Successfully!", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(getBaseContext(),"Fail Update Product", Toast.LENGTH_LONG).show();
                                }
                            } else {
                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("productid", product_id);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getOneProduct")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject product_object = result.getAsJsonObject("productInfo");

                                videourl = product_object.get("video").getAsString();
                                price = product_object.get("price").getAsString();
                                title = product_object.get("title").getAsString();
                                description = product_object.get("information").getAsString();
                                status = product_object.get("status").getAsString();
                                setData();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        _videoview.setVideoURI(Uri.parse(Common.getInstance().getBaseURL() + videourl));
        _videoview.start();
        _videoview.requestFocus();
        _videoview.setKeepScreenOn(true);
        _videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        _priceTxt.setText(price);
        _titleTxt.setText(title);
        _descriptionTxt.setText(description);
        if(status.equals("new")){
            _badgeImg.setVisibility(View.INVISIBLE);
        }else{
            _badgeImg.setVisibility(View.VISIBLE);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            //Your Code
            sel_video = "yes";
            _videoview.setVideoPath(mPaths.get(0));
            _videoview.start();
            _videoview.setVisibility(View.VISIBLE);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getApplicationContext(), Uri.parse(mPaths.get(0)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            durationMs = Integer.parseInt(time);
            retriever.release();
        }
    }
    public boolean validate() {
        boolean valid = true;
        if (title.isEmpty()) {
            _titleTxt.setError("Input Product Title");
            valid = false;
        }else{
            _titleTxt.setError(null);
        }

        if (price.isEmpty()) {
            _priceTxt.setError("Input Price");
            valid = false;
        } else {
            _priceTxt.setError(null);
        }

        if (description.isEmpty()) {
            _descriptionTxt.setError("Input product description");
            valid = false;
        } else {
            _descriptionTxt.setError(null);
        }
        if(sel_video.equals("yes")){
            if (durationMs > 60000){
                Toast.makeText(getBaseContext(), "Product Video Duration is too long, Must be less than 1min", Toast.LENGTH_LONG).show();
                valid = false;
            }
        }

        return valid;
    }
}