package com.example.turismoreal.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.turismoreal.LandingPage;
import com.example.turismoreal.R;

public class CheckInMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
    }

    public void goCheckInPage(View view){
        Intent i = new Intent(this, CheckIn.class);
        startActivity(i);
    }

    public void goBack(View view){
        Intent i = new Intent(this, LandingPage.class);
        startActivity(i);
        finish();
    }
}