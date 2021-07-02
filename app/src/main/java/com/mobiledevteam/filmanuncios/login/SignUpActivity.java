package com.mobiledevteam.filmanuncios.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
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

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private ImageView _userImg;
    private TextView _selImage;
    private EditText _username;
    private EditText _password;
    private EditText _cpassword;
    private EditText _email;
    private EditText _phone;
    private EditText _address;
    private Button _signup;
    private TextView _signin;
    private LatLng my_location;
    private LocationManager locationmanager;
    private String _mSelImg = "no";
    private String filePath;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String cpassword;


    List<Place.Field> fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        _userImg = findViewById(R.id.img_user);
        _selImage = findViewById(R.id.txt_selImage);
        _username = findViewById(R.id.input_username);
        _password = findViewById(R.id.input_password);
        _cpassword = findViewById(R.id.input_cpassword);
        _email = findViewById(R.id.input_email);
        _phone = findViewById(R.id.input_phone);
        _address = findViewById(R.id.input_address);
        _signup = findViewById(R.id.btn_signup);
        _signin = findViewById(R.id.txt_signin);
        setReady();
    }

    private void setReady() {
        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });
        _signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void Signup() {
        username = _username.getText().toString();
        email = _email.getText().toString();
        phone = _phone.getText().toString();
        address = _address.getText().toString();
        password = _password.getText().toString();
        cpassword = _cpassword.getText().toString();
        if(!validate()){
            return;
        }
        if(password.equals(cpassword)){

        }else{
            Toast.makeText(SignUpActivity.this, "Password is not match", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap bm = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String userimage = Base64.encodeToString(b, Base64.DEFAULT);
        


        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();
        json.addProperty("username",username);
        json.addProperty("password",password);
        json.addProperty("email", email);
        json.addProperty("phone",phone);
        json.addProperty("address",address);
        json.addProperty("photo", userimage);
        json.addProperty("latitude",String.valueOf(my_location.latitude));
        json.addProperty("longitude",String.valueOf(my_location.longitude));

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/signup")
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
                                    Toast.makeText(getBaseContext(),"Signup Successfully, please login!", Toast.LENGTH_LONG).show();
                                }else if (status.equals("userexist")) {
                                    Toast.makeText(getBaseContext(),"Username already exist, change username!", Toast.LENGTH_LONG).show();
                                }else if (status.equals("existemail")) {
                                    Toast.makeText(getBaseContext(),"Email already exist, Please check again!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getBaseContext(),"Fail signup", Toast.LENGTH_LONG).show();
                                }
                            } else {
                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        if (username.isEmpty()) {
            _username.setError("Input Username");
            valid = false;
        }else{
            _username.setError(null);
        }

        if (password.isEmpty()) {
            _password.setError("Input Password");
            valid = false;
        } else {
            if(password.length() < 6 ){
                _password.setError("Input password more than 6 charactors");
                valid = false;
            }else{
                _password.setError(null);
            }
        }

        if (email.isEmpty()) {
            _email.setError("Input Email");
            valid = false;
        } else {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()){
                _email.setError(null);
            }else{
                _email.setError("Input Email");
                valid = false;
            }
        }

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
        if(_mSelImg.equals("yes")){

        }else {
            Toast.makeText(SignUpActivity.this, "Select User Image", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}