package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class landing_page extends AppCompatActivity {

    private static final Connection connection = splashScreen.getConn();
    private String respuesta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
    }

    public void logout(View view){
        try {
            Statement sql1 = connection.createStatement();
            ResultSet resultSet = sql1.executeQuery("update employee_session set session_id = 'NO SESSION' where user_id = 1");
            resultSet.next();
            connection.commit();
            //Toast.makeText(this, respuesta, Toast.LENGTH_LONG).show();
            /*
            if (user_id > 0){
                Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, landing_page.class);
                startActivity(i);
                connection.close();
                finish();
            }else{
                Toast.makeText(this, "Usuario Incorrecto", Toast.LENGTH_LONG).show();
            }*/

        } catch (SQLException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}