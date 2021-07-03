package com.mobiledevteam.filmanuncios.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.home.HomeActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText _username;
    private EditText _password;
    private TextView _forgot;
    private Button _login;
    private TextView _signup;

    private String username;
    private String password;
    private String loginStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _username = findViewById(R.id.input_username);
        _password = findViewById(R.id.input_password);
        _forgot = findViewById(R.id.txt_forgot);
        _login = findViewById(R.id.btn_signin);
        _signup = findViewById(R.id.txt_signup);
        setReady();
    }

    private void setReady() {
        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
        _signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signin() {
        username = _username.getText().toString();
        password = _password.getText().toString();
        if(!validate()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/login")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String id = result.get("user_id").toString();
                                if(id.equals("\"nouser\"")){
                                    Toast.makeText(getBaseContext(),"You are not registered, Please signup",Toast.LENGTH_LONG).show();
                                }else if (id.equals("\"wrongpassword\"")){
                                    Toast.makeText(getBaseContext(),"Please input correct password",Toast.LENGTH_LONG).show();
                                }else if (id.equals("\"deleted\"")){
                                    Toast.makeText(getBaseContext(),"Your account is deleted, Please contact to support team",Toast.LENGTH_LONG).show();
                                }else if (id.equals("\"waiting\"")){
                                    Toast.makeText(getBaseContext(),"Your account is not accepted, Please conatct to support team",Toast.LENGTH_LONG).show();
                                }else{
                                    JsonObject clinic_object = result.getAsJsonObject("user_id");
                                    String user_id = clinic_object.get("id").getAsString();
                                    Common.getInstance().setUserID(user_id);
                                    Common.getInstance().setLogin_status("yes");
                                    loginStatus = "yes" + " " + user_id;
                                    writeFile();
                                }
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private  void writeFile(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("loginstatus.fms", MODE_PRIVATE);
            fileOutputStream.write(loginStatus.getBytes());
            fileOutputStream.close();

            Intent intent=new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }catch (FileNotFoundException e){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
            _password.setError(null);
        }
        return valid;
    }

}