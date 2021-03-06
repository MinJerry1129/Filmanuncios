package com.mobiledevteam.filmanuncios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.google.android.libraries.places.api.Places;
import com.mobiledevteam.filmanuncios.home.HomeActivity;
import com.mobiledevteam.filmanuncios.model.Category;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Category> mCategory = new ArrayList<>();

    private static final int REQUEST_LOCATION = 111;
    private String loginCheck = "no 1";
    private String login_status = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!Places.isInitialized()){
//            Places.initialize(getBaseContext(),"AIzaSyDx0lfU-akX0HiFDtEUUIJ99rugOB95Ip4");
            Places.initialize(getBaseContext(),"AIzaSyApkOUDEBKVYSBdPv0QNT8kTbonpoK-g4o");
        }
        readFile();
        setReady();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]
                    {ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else{
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToLogin();
                }
            },4000);
        }
    }

    private void setReady() {
        mCategory.add(new Category("0","Coches",R.drawable.coches));
        mCategory.add(new Category("1","Motos",R.drawable.motos));
        mCategory.add(new Category("2","Motor y Accesorios",R.drawable.motor));
        mCategory.add(new Category("3","Moda y Accesorios",R.drawable.moda));
        mCategory.add(new Category("4","Inmobiliaria",R.drawable.inmobiliaria));
        mCategory.add(new Category("5","TV,Audio y Foto",R.drawable.tv));
        mCategory.add(new Category("6","Moviles y Telefonia",R.drawable.moviles));
        mCategory.add(new Category("7","Informatica y Electronica",R.drawable.informatica));
        mCategory.add(new Category("8","Deporte y Ocio",R.drawable.deporte));
        mCategory.add(new Category("9","Bicicletas",R.drawable.bicicletas));
        mCategory.add(new Category("10","Consolas y Videojuegos",R.drawable.consolas));
        mCategory.add(new Category("11","Hogar y Jardin",R.drawable.hogar));
        mCategory.add(new Category("12","Electrodomesticos",R.drawable.electrodomes));
        mCategory.add(new Category("13","Cine, Libros y Musica",R.drawable.cine));
        mCategory.add(new Category("14","Ninos y Bebes",R.drawable.ninos));
        mCategory.add(new Category("15","Coleccionismo",R.drawable.coleccionismo));
        mCategory.add(new Category("16","Construccion y Reformas",R.drawable.construccion));
        mCategory.add(new Category("17","Industria y Agricultura",R.drawable.inductria));
        mCategory.add(new Category("18","Servicios",R.drawable.servocios));
        mCategory.add(new Category("19","Otros",R.drawable.otros));
        Common.getInstance().setmCategory(mCategory);
    }

    private void readFile(){
        try {
            FileInputStream fileInputStream = openFileInput("loginstatus.fms");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while((lines = bufferedReader.readLine()) != null){
                loginCheck = lines;
            }
            setValue();
        }catch (FileNotFoundException e){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setValue(){
        Log.d("login status:::", loginCheck);
        String[] parts = loginCheck.split(" ");
        if (parts[0].equals("no")){
            login_status = "no";
            Log.d("login status:::", loginCheck);
            Common.getInstance().setLogin_status("no");
        }else{
            login_status = "yes";
            Common.getInstance().setLogin_status("yes");
            Common.getInstance().setUserID(parts[1]);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
//        Log.d("grantResultsLength", Integer.toString(requestCode));
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToLogin();
                }
            },3000);
        }else{
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(getResources().getString(R.string.permission_deny))
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finishAffinity();
                            System.exit(0);
                        }
                    }).show();
        }
    }
    private void moveToLogin(){
            Intent intent=new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);
            finish();
    }
}