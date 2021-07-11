package com.mobiledevteam.filmanuncios.you;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.login.SignUpActivity;
import com.mobiledevteam.filmanuncios.model.Product;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInfoActivity extends AppCompatActivity {
    private ImageView _userImg;
    private TextView _selImage;
    private EditText _username;
    private EditText _email;
    private EditText _phone;
    private EditText _address;
    private Button _update;
    private TextView _signin;
    private LatLng my_location;
    private LocationManager locationmanager;
    private String _mSelImg = "no";
    private String filePath;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String imgurl;
    private String user_id;


    List<Place.Field> fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        _userImg = findViewById(R.id.img_user);
        _selImage = findViewById(R.id.txt_selImage);
        _username = findViewById(R.id.input_username);
        _email = findViewById(R.id.input_email);
        _phone = findViewById(R.id.input_phone);
        _address = findViewById(R.id.input_address);
        _update = findViewById(R.id.btn_update);
        user_id = Common.getInstance().getUserID();
        setReady();
        getData();
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("userid", user_id);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getMyinfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
//                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject user_object = result.getAsJsonObject("userInfo");
                                username = user_object.get("username").getAsString();
                                email = user_object.get("email").getAsString();
                                imgurl = user_object.get("photo").getAsString();
                                phone = user_object.get("phone").getAsString();
                                address = user_object.get("address").getAsString();
                                String latitude = user_object.get("latitude").getAsString();
                                String longitude = user_object.get("longitude").getAsString();
                                my_location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

                                setData();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setReady() {
        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        _selImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage();
            }
        });
        _address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(getBaseContext());
                startActivityForResult(intent,101);
            }
        });
    }

    private void update() {
        phone = _phone.getText().toString();
        address = _address.getText().toString();
        if(!validate()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Update user information...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String userimage = "";
        if (_mSelImg.equals("yes")){
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            userimage = Base64.encodeToString(b, Base64.DEFAULT);
        }




        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();
        json.addProperty("userid",user_id);
        json.addProperty("phone",phone);
        json.addProperty("address",address);
        json.addProperty("photo", userimage);
        json.addProperty("selimage", _mSelImg);
        json.addProperty("latitude",String.valueOf(my_location.latitude));
        json.addProperty("longitude",String.valueOf(my_location.longitude));

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/updateuser")
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
                                    Toast.makeText(getBaseContext(),"Update Success", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getBaseContext(),"Fail Update", Toast.LENGTH_LONG).show();
                                }
                            } else {
                            }
                        }
                    });
        }catch(Exception e) {
        }
    }

    private void setData() {
        _phone.setText(phone);
        _email.setText(email);
        _username.setText(username);
        _address.setText(address);
        Glide.with(this).asBitmap().load(Uri.parse(Common.getInstance().getBaseURL() + imgurl)).into(_userImg);
    }

    public void onPickImage() {
        ImagePicker.Companion.with(this).saveDir(Environment.getExternalStorageDirectory()).cropSquare().maxResultSize(400,400).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK && data != null){
                Place place = Autocomplete.getPlaceFromIntent(data);
                _address.setText(place.getAddress());
                my_location = place.getLatLng();
            }
        }else{
            if(resultCode == Activity.RESULT_OK){
                Uri fileUri = data.getData();
                _mSelImg = "yes";
//                _txtSelImage.setVisibility(View.GONE);
                _userImg.setImageURI(fileUri);
                File file = ImagePicker.Companion.getFile(data);
                filePath = ImagePicker.Companion.getFilePath(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;

        if (phone.isEmpty()) {
            _phone.setError("Input whatsappnumber");
            valid = false;
        } else {
            String expression = "^[+]+[0-9]{9,20}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(phone);
            if (matcher.matches()){
                _phone.setError(null);
            }else{
                _phone.setError("Input correct whatsapp number");
                valid = false;
            }
        }

        if (address.isEmpty()) {
            _address.setError("Input Address");
            valid = false;
        } else {
            _address.setError(null);
        }

        return valid;
    }

}