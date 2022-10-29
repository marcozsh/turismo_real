package com.example.turismoreal.administrator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.models.DepartmentInventory;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.SplashScreen;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DepartmentDetail extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private ScrollView container;
    private ScrollView container2;
    private LinearLayout inventoryContainer;

    private ImageView departmentImage;

    private TextView communeGoBack, address, qtyRooms, status, lastMaintenance, departmentPrice, type, btnAddInventory;

    private Button informationBtn, inventoryBtn,btnAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        container = findViewById(R.id.container);
        container2 = findViewById(R.id.container2);
        inventoryContainer = findViewById(R.id.inventoryContainer);
        btnAddInventory = findViewById(R.id.btnAddInventory);
        btnAvailable = findViewById(R.id.btnAvailable);

        communeGoBack = findViewById(R.id.communeGoBack);
        address = findViewById(R.id.address);
        departmentImage = findViewById(R.id.departmentImageDetail);
        qtyRooms = findViewById(R.id.qtyRooms);
        status = findViewById(R.id.status);
        lastMaintenance = findViewById(R.id.lastMaintenance);
        departmentPrice = findViewById(R.id.departmentPrice);
        type = findViewById(R.id.type);

        informationBtn = findViewById(R.id.informationBtn);
        inventoryBtn = findViewById(R.id.inventoryBtn);

        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        Integer isNew = preferences.getInt("is_new", -1);


        if(isNew == 0){
            btnAvailable.setVisibility(View.INVISIBLE);
        }

        informationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventoryBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                informationBtn.setTextColor(Color.parseColor("#FFFFFF"));
                inventoryBtn.setTextColor(Color.parseColor("#0090FF"));
                informationBtn.setBackgroundColor(Color.parseColor("#0090FF"));
                container2.setVisibility(View.INVISIBLE);
                container.setVisibility(View.VISIBLE);
                btnAddInventory.setVisibility(View.INVISIBLE);
            }
        });

        inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
                Integer id = preferences.getInt("id", 0);
                informationBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                inventoryBtn.setTextColor(Color.parseColor("#FFFFFF"));
                informationBtn.setTextColor(Color.parseColor("#0090FF"));
                inventoryBtn.setBackgroundColor(Color.parseColor("#0090FF"));
                container2.setVisibility(View.VISIBLE);
                btnAddInventory.setVisibility(View.VISIBLE);
                container.setVisibility(View.INVISIBLE);
                getInventory(id);
            }
        });


        try {
            getDepartment();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void getInventory(Integer id){
        inventoryContainer.removeAllViews();
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SplashScreen.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DepartmentService departmentService = retrofit.create(DepartmentService.class);
        String jsonData = "{\"department_id\":"+id+"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
        departmentService.getDepartmentInventory(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    DepartmentInventory[] departmentInventory = new Gson().fromJson(response.body().string(), DepartmentInventory[].class);
                    if(departmentInventory.length == 0){
                        Toast.makeText(DepartmentDetail.this, "Departamento sin inventario",Toast.LENGTH_SHORT).show();
                        return;
                    };
                    for (DepartmentInventory inventory : departmentInventory){
                        TableLayout titleTableLauout = new TableLayout(DepartmentDetail.this);
                        TableRow titleBox = new TableRow(DepartmentDetail.this);
                        TextView spaceEnter = new TextView(DepartmentDetail.this);

                        spaceEnter.setText("Producto: ");
                        spaceEnter.setTextColor(Color.parseColor("#0090FF"));
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(spaceEnter);
                        inventoryContainer.addView(titleTableLauout);

                        TableLayout principalTable = new TableLayout(DepartmentDetail.this);
                        TableRow containerRow = new TableRow(DepartmentDetail.this);
                        TableRow containerRow2 = new TableRow(DepartmentDetail.this);
                        TableRow containerRow3 = new TableRow(DepartmentDetail.this);
                        TableRow containerRow4 = new TableRow(DepartmentDetail.this);
                        containerRow.setGravity(Gravity.LEFT);
                        containerRow2.setGravity(Gravity.LEFT);
                        containerRow3.setGravity(Gravity.LEFT);
                        containerRow4.setGravity(Gravity.LEFT);
                        TableRow atributeRow = new TableRow(DepartmentDetail.this);
                        TableRow atributeRow2 = new TableRow(DepartmentDetail.this);
                        TableRow atributeRow3 = new TableRow(DepartmentDetail.this);
                        TableRow atributeRow4 = new TableRow(DepartmentDetail.this);

                        TableRow spaceRow = new TableRow(DepartmentDetail.this);
                        TableRow spaceRow2 = new TableRow(DepartmentDetail.this);
                        TableRow spaceRow3 = new TableRow(DepartmentDetail.this);
                        TableRow spaceRow4 = new TableRow(DepartmentDetail.this);

                        TableRow valueRow = new TableRow(DepartmentDetail.this);
                        TableRow valueRow2 = new TableRow(DepartmentDetail.this);
                        TableRow valueRow3 = new TableRow(DepartmentDetail.this);
                        TableRow valueRow4 = new TableRow(DepartmentDetail.this);

                        TextView atribute1 = new TextView(DepartmentDetail.this);
                        TextView atribute2 = new TextView(DepartmentDetail.this);
                        TextView atribute3 = new TextView(DepartmentDetail.this);
                        TextView atribute4 = new TextView(DepartmentDetail.this);

                        TextView space = new TextView(DepartmentDetail.this);

                        TextView value1 = new TextView(DepartmentDetail.this);
                        TextView value2 = new TextView(DepartmentDetail.this);
                        TextView value3 = new TextView(DepartmentDetail.this);
                        TextView value4 = new TextView(DepartmentDetail.this);

                        atribute1.setText("Nombre");
                        atribute2.setText("Marca");
                        atribute3.setText("Tipo de producto");
                        atribute4.setText("Cantidad");

                        space.setText("                       ");

                        value1.setTextColor(Color.parseColor("#000000"));
                        value2.setTextColor(Color.parseColor("#000000"));
                        value3.setTextColor(Color.parseColor("#000000"));
                        value4.setTextColor(Color.parseColor("#000000"));

                        String name = inventory.getName();
                        value1.setText(name);
                        String brand = inventory.getBrand();
                        value2.setText(brand);
                        String productType = inventory.getProductType();
                        value3.setText(productType);
                        Integer qty = inventory.getQty();
                        value4.setText(qty.toString());

                        inventoryContainer.addView(principalTable);

                        principalTable.addView(containerRow);
                        principalTable.addView(containerRow2);
                        principalTable.addView(containerRow3);
                        principalTable.addView(containerRow4);

                        containerRow.addView(atributeRow);
                        containerRow2.addView(atributeRow2);
                        containerRow3.addView(atributeRow3);
                        containerRow4.addView(atributeRow4);

                        containerRow.addView(spaceRow);
                        containerRow2.addView(spaceRow2);
                        containerRow3.addView(spaceRow3);
                        containerRow4.addView(spaceRow4);

                        containerRow.addView(valueRow);
                        containerRow2.addView(valueRow2);
                        containerRow3.addView(valueRow3);
                        containerRow4.addView(valueRow4);

                        atributeRow.addView(atribute1);
                        atributeRow2.addView(atribute2);
                        atributeRow3.addView(atribute3);
                        atributeRow4.addView(atribute4);

                        valueRow.addView(value1);
                        valueRow2.addView(value2);
                        valueRow3.addView(value3);
                        valueRow4.addView(value4);
                        spaceRow.addView(space);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(DepartmentDetail.this, "Departamento sin inventario",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getDepartment() throws ParseException {

        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        Integer id = preferences.getInt("id", 0);
        String departmentImageBs64 = preferences.getString("department_image", "NO PHOTO");
        int qty_rooms = preferences.getInt("qty_rooms", 0);
        String department_type = preferences.getString("department_type", "SIN REGISTRO");
        int price = preferences.getInt("price", 0);
        String commune = preferences.getString("commune", "SIN REGISTRO");
        String departmentAddress = preferences.getString("address", "SIN REGISTRO");
        int isNew = preferences.getInt("is_new", -1);
        String check_in = preferences.getString("check_in", "1900/01/01");
        String check_out = preferences.getString("check_out", "1900/01/01");
        String maintenance_start = preferences.getString("maintenance_start", "1900/01/01");
        String maintenance_finish = preferences.getString("maintenance_finish", "1900/01/01");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        String todaysDateString = sdf.format(today);
        Date todaysDate = new SimpleDateFormat("dd/MM/yyyy").parse(todaysDateString);

        String departmentStatus = "Disponible";
        if (!check_in.isEmpty()){
            Date chk_in = new SimpleDateFormat("dd/MM/yyyy").parse(check_in);
            Date chk_out = new SimpleDateFormat("dd/MM/yyyy").parse(check_out);
            Date maintenance_s = new SimpleDateFormat("dd/MM/yyyy").parse(maintenance_start);
            Date maintenance_f = new SimpleDateFormat("dd/MM/yyyy").parse(maintenance_finish);
            if ((todaysDate.after(chk_in) && todaysDate.before(chk_out)) || todaysDate.equals(chk_in)) {
                departmentStatus = "Reservado";
            }else if ((todaysDate.after(maintenance_s) && todaysDate.before(maintenance_f)) || todaysDate.equals(maintenance_s)){
                departmentStatus = "En mantenimiento";
            }
        }
        if (isNew != -1){
            departmentStatus = isNew == 1 ? "Nuevo" : departmentStatus;
            status.setText(departmentStatus);
        }
        if (!maintenance_finish.equals("1900/01/01")){
            Date maintenance_f = new SimpleDateFormat("dd/MM/yyyy").parse(maintenance_finish);
            if(todaysDate.after(maintenance_f)){
                lastMaintenance.setText(maintenance_finish);
            }else {
                lastMaintenance.setText("SIN REGISTRO");
            }
        }else{
            lastMaintenance.setText("SIN REGISTRO");
        }
        communeGoBack.setText(commune);
        address.setText(departmentAddress + ", " + commune);
        qtyRooms.setText(Integer.toString(qty_rooms));
        byte [] bytes = Base64.decode(departmentImageBs64, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
        departmentImage.setImageBitmap(bitmap);
        departmentPrice.setText("$"+ price);
        type.setText(department_type);
    }


    public void registerMaintenance(View view){
        Intent i = new Intent(this, MaintenanceRegister.class);
        startActivity(i);
    }

    public void addDepartmentInventory(View view){
        Intent i = new Intent(this, AddDepartmentInventory.class);
        startActivity(i);
    }


    public void departmentAvailable(View view){
        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        Integer id = preferences.getInt("id", 0);
        Integer isNew = preferences.getInt("is_new", -1);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        final View loading = getLayoutInflater().inflate(R.layout.loading_gif, null);
        dialogBuilder.setView(loading);
        dialog = dialogBuilder.create();
        dialog.show();
        if (isNew == -1) {
            Toast.makeText(DepartmentDetail.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }else{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DepartmentService departmentService = retrofit.create(DepartmentService.class);
            String jsonData = "{\"department_id\":"+id+"}";
            RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
            departmentService.markDepartmentAvailable(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        OneResponse responsePost = new Gson().fromJson(response.body().string(), OneResponse.class);
                        int departmentId = responsePost.getResponse();
                        if(departmentId != 0){
                            Toast.makeText(DepartmentDetail.this, "Departamento marcado como disponible", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());

                        }else{
                            Toast.makeText(DepartmentDetail.this, "Error al marcar como dispoible", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {}
            });
        }

    }

    public void extraServicesMenu(View view){
        Intent i = new Intent(this, ExtraServicePage.class);
        startActivity(i);
        finish();
    }

    public void goBack(View view){
        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        finish();
    }

}