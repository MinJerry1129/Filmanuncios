package com.mobiledevteam.filmanuncios.upload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UploadProductActivity extends AppCompatActivity {
    private LinearLayout _videoLayout;
    private VideoView _productVideo;
    private TextView _selvideoTxt;
    private Button _uploadBtn;
    private EditText _titleTxt;
    private EditText _priceTxt;
    private EditText _descriptionTxt;

    private  String category_id;
    private  String user_id;
    private int durationMs;
    private String title;
    private String price;
    private String description;
    private String sel_video = "no";
    private List<String> mPaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        _videoLayout = (LinearLayout) findViewById(R.id.layout_video);
        _productVideo = (VideoView) findViewById(R.id.video_product);
        _selvideoTxt = (TextView) findViewById(R.id.txt_selvideo);
        _uploadBtn= (Button) findViewById(R.id.btn_upload);
        _titleTxt = (EditText)findViewById(R.id.input_title);
        _priceTxt = (EditText)findViewById(R.id.input_price);
        _descriptionTxt = (EditText)findViewById(R.id.input_description);

        category_id = Common.getInstance().getUpload_category_id();
        user_id = Common.getInstance().getUserID();
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
        _uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadProduct();
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

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Add Product...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();
        json.addProperty("userid",user_id);
        json.addProperty("categoryid",category_id);
        json.addProperty("title",title);
        json.addProperty("price",price);
        json.addProperty("description",description);
        json.addProperty("video", base64);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/addproduct")
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
                                    Toast.makeText(getBaseContext(),"Add Product Successfully!", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    Toast.makeText(getBaseContext(),"Fail Add Product", Toast.LENGTH_LONG).show();
                                }
                            } else {
                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            mPaths =  data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            //Your Code
            sel_video = "yes";
            _productVideo.setVideoPath(mPaths.get(0));
            _productVideo.start();
            _productVideo.setVisibility(View.VISIBLE);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getApplicationContext(), Uri.parse(mPaths.get(0)));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            durationMs = Integer.parseInt(time);
            retriever.release();
//            retriever.close();
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
        if(sel_video.equals("no")){
            Toast.makeText(getBaseContext(), "Please select the product video", Toast.LENGTH_LONG).show();
            valid = false;
        }else{
            if (durationMs > 60000){
                Toast.makeText(getBaseContext(), "Product Video Duration is too long, Must be less than 1min", Toast.LENGTH_LONG).show();
                valid = false;
            }
        }

        return valid;
    }
}