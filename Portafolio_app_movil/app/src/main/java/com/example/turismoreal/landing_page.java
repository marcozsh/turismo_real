package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class landing_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
    }
}