package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

@SuppressLint("CustomSplashScreen")
public class splashScreen extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.0.6:1522:XE";
    private static final String USERNAME = "turismo_real_dev";
    private static final String PASSWORD = "123";
    private static Connection connection;

    private Integer session_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        new Handler().postDelayed(() -> {
            try{
                connection = getConn();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT count(session_id) FROM employee_session WHERE user_id = 1");
                while (resultSet.next()){
                    session_id = Integer.parseInt(resultSet.getString(1));
                }
                if (session_id <= 0){
                    Intent i = new Intent(this, login.class);
                    startActivity(i);
                    connection.close();
                    finish();
                }else {
                    Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, landing_page.class);
                    startActivity(i);
                    connection.close();
                    finish();
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, 2000);
    }


    public static Connection getConn() {
        try
        {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection != null)
            {
                return connection;
            } else
            {
                return null;
            }
        } catch (ClassNotFoundException | SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }

    }
}