package com.example.turismoreal;

import static com.example.turismoreal.login.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class landing_page extends AppCompatActivity {

    private static final Connection connection = splashScreen.getConn();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView logutMessage;
    private Button yes, no;

    private TextView userRol;
    private TextView fullName;

    private Integer user_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String rol = preferences.getString("userRol", "NO SESSION");
        String userName = preferences.getString("fullName", "NO SESSION");
        Integer userId = preferences.getInt("userId", 0);

        userRol = findViewById(R.id.txtRol);
        fullName = findViewById(R.id.userName);
        userRol.setText(rol);
        fullName.setText(userName);
        user_id = userId;
    }

    public void popOutLogout(View view){
        dialogBuilder = new AlertDialog.Builder(this);
        final View logoutView = getLayoutInflater().inflate(R.layout.logout_popout, null);
        logutMessage = (TextView) logoutView.findViewById(R.id.logoutMessage);
        yes = (Button) logoutView.findViewById(R.id.btnYes);
        no = (Button) logoutView.findViewById(R.id.btnNo);

        dialogBuilder.setView(logoutView);
        dialog = dialogBuilder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(landing_page.this, "Saliendo", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void logout(){
        try {
            Statement sql1 = connection.createStatement();
            sql1.executeQuery("update employee_session set session_id = 'NO SESSION' where user_id = "+ user_id);
            connection.commit();
            SharedPreferences preferences = getSharedPreferences("current_session", Context.MODE_PRIVATE);
            preferences.edit().clear().apply();
            Intent i = new Intent(landing_page.this, login.class);
            startActivity(i);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }



    public void extraServicesMenu(View view){
        Intent i = new Intent(this, extra_services.class);
        startActivity(i);
        finish();
    }
}