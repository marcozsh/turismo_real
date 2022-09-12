package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class addExtraService extends AppCompatActivity {


    private Switch swicthButton;
    private EditText serviceName;
    private EditText servicePrice;
    private EditText serviceLocation;
    private RadioButton serviceTypeTour;
    private RadioButton serviceTypeTransport;

    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_extra_service);

        swicthButton = findViewById(R.id.switchAvaible);
        serviceName = findViewById(R.id.serviceName);
        servicePrice = findViewById(R.id.servicePrice);
        serviceLocation = findViewById(R.id.serviceLocation);
        serviceTypeTour = findViewById(R.id.serviceTypeTours);
        serviceTypeTour.setChecked(true);
        serviceTypeTransport = findViewById(R.id.serviceTransportType);


    }

    public void serviceType(View view) {
        if (serviceTypeTour.isChecked()) {
            serviceTypeTransport.setChecked(false);
        }
    }
    public void setServiceTypeTransport(View view) {
        if (serviceTypeTour.isChecked()) {
            serviceTypeTour.setChecked(false);
        }
    }
    public void addService(View view){
        String name = serviceName.getText().toString();
        Integer price = Integer.parseInt(servicePrice.getText().toString());
        String location = serviceLocation.getText().toString();
        Integer avalible = swicthButton.isChecked() ? 1 : 0;
        Integer serviceType = serviceTypeTour.isChecked() ? 1 : 2;
        Integer last_id = 0;
        try {
            Statement sql = connection.createStatement();
            ResultSet result = sql.executeQuery("SELECT fn_add_service("+ serviceType + ", '"+name+"', "+price+", '"+location+"', "+avalible+") as last_service_id FROM DUAL");
            while(result.next()){
                last_id = result.getInt(1);
            }
            if (last_id != 0) {
                connection.commit();
                Toast.makeText(this, "Servicio Agregado correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, extra_services.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(this, "Error al agregar el servicio extra", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }


    public void switchButton(View view){
        if (swicthButton.isChecked()){
            swicthButton.setText("Disponible");
        }else{
            swicthButton.setText("No Disponible");
        }

    }

    public void goBack(View view){
        Intent i = new Intent(this, extra_services.class);
        startActivity(i);
        finish();
    }
}