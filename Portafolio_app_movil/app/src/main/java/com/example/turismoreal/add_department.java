package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class add_department extends AppCompatActivity {

    private Switch switchButton;
    private Spinner communeSpinner;
    private Spinner departmentTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        switchButton = findViewById(R.id.switchAvaible);
        communeSpinner = findViewById(R.id.communeSpinner);
        departmentTypeSpinner = findViewById(R.id.departmentTypeSpinner);


        String [] departmentType = {"LOFT", "STUDIO", "BASIC", "FAMILIAR", "PENHOUSE"};

        ArrayAdapter<String> departmentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentType);

        departmentTypeSpinner.setAdapter(departmentTypeAdapter);

        String [] options = {"Provi", "San Beca", "hola","Provi", "San Beca", "hola", "San Beca", "hola","Provi", "San Beca", "hola", "San Beca", "hola","Provi", "San Beca", "hola", "San Beca", "hola","Provi", "San Beca", "hola"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);

        communeSpinner.setAdapter(adapter);
    }

    public void goBack(View view){
        Intent i = new Intent(this, department_page.class);
        startActivity(i);
        finish();
    }

    public void switchButton(View view){
        if (switchButton.isChecked()){
            switchButton.setText("Disponible");
        }else{
            switchButton.setText("No Disponible");
        }

    }
}