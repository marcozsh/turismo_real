package com.example.turismoreal.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;

public class ReservationExtraServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_extra_services);
    }

    public void goBack(View view){
        Intent i = new Intent(this, CheckIn.class);
        startActivity(i);
        finish();
    }
}