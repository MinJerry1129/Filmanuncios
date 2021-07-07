package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.cell.CategoryListAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeFeatureProductAdapter;
import com.mobiledevteam.filmanuncios.cell.HomeNearProductAdapter;
import com.mobiledevteam.filmanuncios.favorite.FavHomeActivity;
import com.mobiledevteam.filmanuncios.inbox.InboxHomeActivity;
import com.mobiledevteam.filmanuncios.login.LoginActivity;
import com.mobiledevteam.filmanuncios.model.Category;
import com.mobiledevteam.filmanuncios.model.FavProduct;
import com.mobiledevteam.filmanuncios.model.FavUser;
import com.mobiledevteam.filmanuncios.model.Product;
import com.mobiledevteam.filmanuncios.upload.UploadHomeActivity;
import com.mobiledevteam.filmanuncios.you.YouHomeActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements CategoryListAdapter.OnItemClicked, LocationListener {
    private LinearLayout _menuHome;
    private LinearLayout _menuFav;
    private LinearLayout _menuUpload;
    private LinearLayout _menuInbox;
    private LinearLayout _menuYou;
    private RecyclerView _categoryReycle;
    private RecyclerView _featureproductRecycle;
    private GridView _nearproductGrid;
    ArrayList<Category> mAllCategoryList = new ArrayList<>();
    ArrayList<Product> mAllFeatureProductList = new ArrayList<>();
    ArrayList<Product> mAllNearProductList = new ArrayList<>();
    ArrayList<Product> mAllProduct = new ArrayList<>();

    private LocationManager locationmanager;
    private String user_id;
    private String login_status = "no";
    private Location my_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);



        _menuHome= (LinearLayout)findViewById(R.id.menu_home);
        _menuFav= (LinearLayout)findViewById(R.id.menu_fav);
        _menuUpload= (LinearLayout)findViewById(R.id.menu_upload);
        _menuInbox= (LinearLayout)findViewById(R.id.menu_inbox);
        _menuYou= (LinearLayout)findViewById(R.id.menu_you);

        _categoryReycle = (RecyclerView)findViewById(R.id.recycler_category);
        _featureproductRecycle = (RecyclerView)findViewById(R.id.recycler_feature_product);
        _nearproductGrid = (GridView) findViewById(R.id.grid_near_product);
        LinearLayoutManager layoutManager_category = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_feature_product = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _categoryReycle.setLayoutManager(layoutManager_category);
        _featureproductRecycle.setLayoutManager(layoutManager_feature_product);
        mAllCategoryList = Common.getInstance().getmCategory();

        login_status = Common.getInstance().getLogin_status();
        if (login_status.equals("yes")){
            user_id = Common.getInstance().getUserID();
        }
        setReady();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void setReady() {
        _menuHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoHome();
            }
        });
        _menuFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoFav();
            }
        });
        _menuUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoUpload();
            }
        });
        _menuInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoInbox();
            }
        });
        _menuYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoYou();
            }
        });
        initViewCategory();
        _nearproductGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.getInstance().setProduct_id(mAllNearProductList.get(position).getmId());
                Intent intent = new Intent(getApplicationContext(), OneProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
//        mAllFeatureProductList.add(new Product("1","1","Porsche Boxster car", "1234567890","300"));
//        mAllNearProductList.add(new Product("4","1","Porsche Boxster car", "1234567890","300"));

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Ion.with(HomeActivity.this)
                            .load(Common.getInstance().getBaseURL()+"api/getHomeData")
                            .setJsonObjectBody(json)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    progressDialog.dismiss();
                                    Log.d("result::", result.toString());
                                    if (result != null) {
                                        mAllFeatureProductList = new ArrayList<>();
                                        mAllNearProductList = new ArrayList<>();
                                        mAllProduct = new ArrayList<>();
                                        JsonArray product_array = result.get("productsInfo").getAsJsonArray();
                                        for(JsonElement productElement : product_array){
                                            JsonObject theOne = productElement.getAsJsonObject();
                                            String id = theOne.get("id").getAsString();
                                            String userid = theOne.get("userid").getAsString();
                                            String categoryid = theOne.get("categoryid").getAsString();
                                            String title = theOne.get("title").getAsString();
                                            String price = theOne.get("price").getAsString();
                                            String video = theOne.get("video").getAsString();
                                            String latitude = theOne.get("latitude").getAsString();
                                            String longitude = theOne.get("longitude").getAsString();
                                            String pdate = theOne.get("publicdate").getAsString();
                                            String status = theOne.get("status").getAsString();
                                            LatLng plocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                            if(status.equals("badge")){
                                                mAllFeatureProductList.add(new Product(id,userid,categoryid,title,video,price,plocation,pdate,status));
                                            }
//
                                            mAllNearProductList.add(new Product(id,userid,categoryid,title,video,price,plocation,pdate,status));
                                            mAllProduct.add(new Product(id,userid,categoryid,title,video,price,plocation,pdate,status));
                                        }
                                        Common.getInstance().setmAllProduct(mAllProduct);
                                        sortList();
                                        FeatureList();

                                    } else {

                                    }
                                }
                            });
                }catch(Exception e){
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },6000);
    }

    private void FeatureList() {
        initViewFeatureProduct();
    }

    private void sortList() {
        if (mAllNearProductList.size() >1){

            for (int i=0; i<mAllNearProductList.size()-1; i++){
                float distance1 = calculateDistance(mAllNearProductList.get(i));
                for(int j=i+1; j<mAllNearProductList.size(); j++){
                    float distance2 = calculateDistance(mAllNearProductList.get(j));
                    if (distance1 > distance2){
                        Product oneproduct = new Product(mAllNearProductList.get(i).getmId(), mAllNearProductList.get(i).getmCompanyId(),mAllNearProductList.get(i).getmCategoryID(),mAllNearProductList.get(i).getmName(), mAllNearProductList.get(i).getmVideoID(), mAllNearProductList.get(i).getmPrice(), mAllNearProductList.get(i).getmLocation(), mAllNearProductList.get(i).getmPDate(), mAllNearProductList.get(i).getmStatus());
                        mAllNearProductList.get(i).setmId(mAllNearProductList.get(j).getmId());
                        mAllNearProductList.get(i).setmCompanyId(mAllNearProductList.get(j).getmCompanyId());
                        mAllNearProductList.get(i).setmCategoryID(mAllNearProductList.get(j).getmCategoryID());
                        mAllNearProductList.get(i).setmName(mAllNearProductList.get(j).getmName());
                        mAllNearProductList.get(i).setmPrice(mAllNearProductList.get(j).getmPrice());
                        mAllNearProductList.get(i).setmVideoID(mAllNearProductList.get(j).getmVideoID());
                        mAllNearProductList.get(i).setmLocation(mAllNearProductList.get(j).getmLocation());
                        mAllNearProductList.get(i).setmPDate(mAllNearProductList.get(j).getmPDate());
                        mAllNearProductList.get(i).setmStatus(mAllNearProductList.get(j).getmStatus());

                        mAllNearProductList.get(j).setmId(oneproduct.getmId());
                        mAllNearProductList.get(j).setmCompanyId(oneproduct.getmCompanyId());
                        mAllNearProductList.get(j).setmCategoryID(oneproduct.getmCategoryID());
                        mAllNearProductList.get(j).setmName(oneproduct.getmName());
                        mAllNearProductList.get(j).setmPrice(oneproduct.getmPrice());
                        mAllNearProductList.get(j).setmVideoID(oneproduct.getmVideoID());
                        mAllNearProductList.get(j).setmLocation(oneproduct.getmLocation());
                        mAllNearProductList.get(j).setmPDate(oneproduct.getmPDate());
                        mAllNearProductList.get(j).setmStatus(oneproduct.getmStatus());
                    }
                }
            }
        }
        initViewNearProduct();
    }
    private float calculateDistance(Product oneProduct){
        Location loc = new Location("");
        loc.setLatitude(oneProduct.getmLocation().latitude);
        loc.setLongitude(oneProduct.getmLocation().longitude);
        float distanceInMeters = my_location.distanceTo(loc);
        return distanceInMeters;
    }

    private void initViewFeatureProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                HomeFeatureProductAdapter adapter_product = new HomeFeatureProductAdapter(getBaseContext(), mAllFeatureProductList);
                _featureproductRecycle.setAdapter(adapter_product);
//                adapter_brand.setOnClick(AllProductActivity.this);
            }
        });
    }
    private void initViewNearProduct(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                int count_product = mAllNearProductList.size()/2;
                if(mAllNearProductList.size() % 2 == 1){
                    count_product = count_product + 1;
                }
                ViewGroup.LayoutParams layoutParams = _nearproductGrid.getLayoutParams();
                layoutParams.height = convertDpToPixels(225,getBaseContext()) * count_product; //this is in pixels
                _nearproductGrid.setLayoutParams(layoutParams);

                HomeNearProductAdapter adapter_product = new HomeNearProductAdapter(getBaseContext(), mAllNearProductList);
                _nearproductGrid.setAdapter(adapter_product);
            }
        });
    }

    private void initViewCategory(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                CategoryListAdapter adapter_category = new CategoryListAdapter(getBaseContext(), mAllCategoryList);
                _categoryReycle.setAdapter(adapter_category);
                adapter_category.setOnClick(HomeActivity.this);
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Common.getInstance().setSelcategoryID(String.valueOf(position));
        Common.getInstance().setMinvalue("0");
        Common.getInstance().setMaxvalue("1000000");
        Common.getInstance().setMy_location(my_location);
        Common.getInstance().setDuration("250000");
        Common.getInstance().setPostdate("2021-01-01");
        Intent intent = new Intent(getApplicationContext(), CategoryProductActivity.class);
        startActivity(intent);
        Log.d("position", String.valueOf(position));
    }


    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    private void onGoHome(){

    }
    private void onGoFav(){
        if (login_status.equals("no")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), FavHomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private void onGoUpload(){
        if (login_status.equals("no")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), UploadHomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void onGoInbox(){
        if (login_status.equals("no")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), InboxHomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
    private void onGoYou(){
        if (login_status.equals("no")){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(getApplicationContext(), YouHomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onLocationChanged(Location location) {
        my_location = location;
        Log.d("Location:::", String.valueOf( location.getLatitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}