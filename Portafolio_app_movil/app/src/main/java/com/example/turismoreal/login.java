package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

//conn class
import com.example.turismoreal.oracleConnection;

//sql imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//login interface
import com.example.turismoreal.landing_page;

public class login extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Integer user_id;


    //private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    //private static final String URL = "jdbc:oracle:thin:@192.168.0.6:1522:XE";
    //private static final String USERNAME = "turismo_real_dev";
    //private static final String PASSWORD = "123";
    private Connection connection;

    {
        connection = oracleConnection.getConn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user = findViewById(R.id.username);
        password = findViewById(R.id.password);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

    }
    public void buttonConnectToOracleDB(View view){
        String username = user.getText().toString();
        String pass =password.getText().toString();
        if (!username.isEmpty() && !pass.isEmpty()){
            try {
                Statement sql1 = connection.createStatement();
                ResultSet resultSet = sql1.executeQuery("SELECT fn_login('"+username+"', '"+pass+"') as respuesta FROM DUAL");
                while (resultSet.next()){
                    user_id = Integer.parseInt(resultSet.getString(1));
                }
                connection.commit();
                //Toast.makeText(this, user_id.toString(), Toast.LENGTH_LONG).show();
                if (user_id > 0){
                    Toast.makeText(this, "Bienvenido!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, landing_page.class);
                    startActivity(i);
                    connection.close();
                    finish();
                }else{
                    Toast.makeText(this, "Usuario Incorrecto", Toast.LENGTH_LONG).show();
                }

            }
            catch (SQLException e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_LONG).show();
        }
    }

}