package com.mobiledevteam.filmanuncios.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobiledevteam.filmanuncios.Common;
import com.mobiledevteam.filmanuncios.R;
import com.mobiledevteam.filmanuncios.model.Category;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CategoryFilterActivity extends AppCompatActivity {
    private Spinner _categorySpinner, _locationSpinner;
    private ImageView _categoryImg;
    private TextView _startDateTxt;
    private Calendar mCalendar;
    private RangeSeekBar _priceSeekBar;

    private int minprice = 0;
    private int maxPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);
        _categorySpinner = (Spinner) findViewById(R.id.spinner_category);
        _locationSpinner = (Spinner) findViewById(R.id.spinner_location);
        _startDateTxt = (TextView)findViewById(R.id.txt_startdate);
        _categoryImg = (ImageView)findViewById(R.id.img_category);
        _priceSeekBar = (RangeSeekBar)findViewById(R.id.seek_price);
        mCalendar = Calendar.getInstance();
        setCategorySpineer();
        setLocationSpinner();
        setReady();
    }

    public void setCategorySpineer() {
        List<String> list = new ArrayList<String>();
        for(Category oneCategory : Common.getInstance().getmCategory())
        {
            list.add(oneCategory.getmName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _categorySpinner.setAdapter(dataAdapter);
        _categorySpinner.setSelection(1);
    }

    public void setLocationSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("No Limit Distance");
        list.add("1 km");
        list.add("5 km");
        list.add("10 km");
        list.add("50 km");
        list.add("100 km");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _locationSpinner.setAdapter(dataAdapter);
        _locationSpinner.setSelection(0);
    }

    private void setReady() {
        _categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _categoryImg.setImageResource(Common.getInstance().getmCategory().get(position).getmImgurl());
//                Toast.makeText(getBaseContext(), String.valueOf(_priceSeekBar.getSelectedMinValue()), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        _locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        _startDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CategoryFilterActivity.this, date,mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        _startDateTxt.setText(sdf.format(mCalendar.getTime()));
    }
}