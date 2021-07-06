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
//        _nearproductGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), OneProductActivity.class);
//                startActivity(intent);
//            }
//        });
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
                                        JsonArray product_array = result.get("productsInfo").getAsJsonArray();
                                        for(JsonElement productElement : product_array){
                                            JsonObject theOne = productElement.getAsJsonObject();
                                            String id = theOne.get("id").getAsString();
                                            String userid = theOne.get("userid").getAsString();
                                            String title = theOne.get("title").getAsString();
                                            String price = theOne.get("price").getAsString();
                                            String video = theOne.get("video").getAsString();
                                            String latitude = theOne.get("latitude").getAsString();
                                            String longitude = theOne.get("longitude").getAsString();
                                            String status = theOne.get("status").getAsString();
                                            LatLng plocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                                            mAllFeatureProductList.add(new Product(id,userid,title,video,price,plocation));
//                                            mAllNearProductList.add(new Product(id,userid,title,video,price,plocation));
                                            mAllProduct.add(new Product(id,userid,title,video,price,plocation,status));
                                        }
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
        if (mAllProduct.size() >1){

            for (int i=0; i<mAllProduct.size()-1; i++){
                float distance1 = calculateDistance(mAllProduct.get(i));
                for(int j=i+1; j<mAllProduct.size(); j++){
                    float distance2 = calculateDistance(mAllProduct.get(j));
                    if (distance1 > distance2){
                        Product oneproduct = new Product(mAllProduct.get(i).getmId(), mAllProduct.get(i).getmCompanyId(),mAllProduct.get(i).getmName(), mAllProduct.get(i).getmVideoID(), mAllProduct.get(i).getmPrice(), mAllProduct.get(i).getmLocation(), mAllProduct.get(i).getmStatus());
                        mAllProduct.get(i).setmId(mAllProduct.get(j).getmId());
                        mAllProduct.get(i).setmCompanyId(mAllProduct.get(j).getmCompanyId());
                        mAllProduct.get(i).setmName(mAllProduct.get(j).getmName());
                        mAllProduct.get(i).setmPrice(mAllProduct.get(j).getmPrice());
                        mAllProduct.get(i).setmVideoID(mAllProduct.get(j).getmVideoID());
                        mAllProduct.get(i).setmLocation(mAllProduct.get(j).getmLocation());
                        mAllProduct.get(i).setmStatus(mAllProduct.get(j).getmStatus());

                        mAllProduct.get(j).setmId(oneproduct.getmId());
                        mAllProduct.get(j).setmCompanyId(oneproduct.getmCompanyId());
                        mAllProduct.get(j).setmName(oneproduct.getmName());
                        mAllProduct.get(j).setmPrice(oneproduct.getmPrice());
                        mAllProduct.get(j).setmVideoID(oneproduct.getmVideoID());
                        mAllProduct.get(j).setmLocation(oneproduct.getmLocation());
                        mAllProduct.get(j).setmStatus(oneproduct.getmStatus());
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
                int count_product = mAllProduct.size()/2;
                if(mAllProduct.size() % 2 == 1){
                    count_product = count_product + 1;
                }
                ViewGroup.LayoutParams layoutParams = _nearproductGrid.getLayoutParams();
                layoutParams.height = convertDpToPixels(225,getBaseContext()) * count_product; //this is in pixels
                _nearproductGrid.setLayoutParams(layoutParams);

                HomeNearProductAdapter adapter_product = new HomeNearProductAdapter(getBaseContext(), mAllProduct);
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