package com.example.turismoreal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class extra_services extends AppCompatActivity {

    private LinearLayout principalLayout;
    private TableRow title;
    private TableRow atributeRow;
    private TableRow spaceRow;
    private TableRow valueRow;

    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_services);
        principalLayout = findViewById(R.id.principalLayout);


        try{
            Statement sql2 = connection.createStatement();
            ResultSet result = sql2.executeQuery("\n" +
                    "SELECT name, price, nvl(location, 'SIN REGISTRO') as location,\n" +
                    "       CASE \n" +
                    "       WHEN available = 1 THEN 'Disponible'\n" +
                    "       ELSE 'No Disponible'\n" +
                    "       END as available\n" +
                    "FROM service ORDER BY id");


            JSONObject servicesName = null;
            ArrayList<JSONObject>  titles = new ArrayList<JSONObject>();

            Integer rowCount = 0;
            while (result.next()){
                 servicesName= new JSONObject();
                 servicesName.put("name", result.getString(1));
                 servicesName.put("price", result.getInt(2));
                 servicesName.put("location", result.getString(3));
                 servicesName.put("available", result.getString(4));
                 titles.add(servicesName);

            }


            //Toast.makeText(this, resul, Toast.LENGTH_SHORT).show();


            for (JSONObject title : titles) {
                //title
                TableLayout titleTableLauout = new TableLayout(this);
                TableRow titleBox = new TableRow(this);
                TextView nombre = new TextView(this);

                nombre.setText(title.getString("name"));
                nombre.setTextColor(Color.parseColor("#0090FF"));

                principalLayout.addView(titleTableLauout);
                titleTableLauout.addView(titleBox);
                titleBox.addView(nombre);

                //atributes
                TableLayout principalTable = new TableLayout(this);
                TableRow containerRow = new TableRow(this);
                TableRow containerRow2 = new TableRow(this);
                TableRow containerRow3 = new TableRow(this);
                containerRow.setGravity(Gravity.LEFT);
                TableRow atributeRow = new TableRow(this);
                TableRow atributeRow2 = new TableRow(this);
                TableRow atributeRow3 = new TableRow(this);

                TableRow spaceRow = new TableRow(this);
                TableRow spaceRow2 = new TableRow(this);
                TableRow spaceRow3 = new TableRow(this);

                TableRow valueRow = new TableRow(this);
                TableRow valueRow2 = new TableRow(this);
                TableRow valueRow3 = new TableRow(this);

                TextView atribute1 = new TextView(this);
                TextView atribute2 = new TextView(this);
                TextView atribute3 = new TextView(this);

                TextView space = new TextView(this);

                TextView value1 = new TextView(this);
                TextView value2 = new TextView(this);
                TextView value3 = new TextView(this);

                atribute1.setText("Localización");
                atribute2.setText("Precio");
                atribute3.setText("Disponibilidad");

                space.setText(" ");

                value1.setTextColor(Color.parseColor("#000000"));
                value2.setTextColor(Color.parseColor("#000000"));
                value3.setTextColor(Color.parseColor("#000000"));

                value1.setText(title.getString("location"));
                String price = "$" + title.getString("price");
                value2.setText(price);
                value3.setText(title.getString("available"));

                principalLayout.addView(principalTable);

                principalTable.addView(containerRow);
                principalTable.addView(containerRow2);
                principalTable.addView(containerRow3);

                containerRow.addView(atributeRow);
                containerRow2.addView(atributeRow2);
                containerRow3.addView(atributeRow3);

                containerRow.addView(spaceRow);
                containerRow2.addView(spaceRow2);
                containerRow3.addView(spaceRow3);

                containerRow.addView(valueRow);
                containerRow2.addView(valueRow2);
                containerRow3.addView(valueRow3);

                atributeRow.addView(atribute1);
                atributeRow2.addView(atribute2);
                atributeRow3.addView(atribute3);

                valueRow.addView(value1);
                valueRow2.addView(value2);
                valueRow3.addView(value3);
                spaceRow.addView(space);
            }
        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }



    }

    public void addService (View view){
        Intent i = new Intent(this, addExtraService.class);
        startActivity(i);
        finish();
    }

    public void goBack(View view){
        Intent i = new Intent(this, landing_page.class);
        startActivity(i);
        finish();
    }
}