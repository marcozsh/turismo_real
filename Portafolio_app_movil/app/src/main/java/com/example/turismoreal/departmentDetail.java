package com.example.turismoreal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.models.Department;
import com.example.turismoreal.models.DepartmentInventory;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class departmentDetail extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView communeGoBack;
    private TextView address;


    private ScrollView container;
    private ScrollView container2;
    private LinearLayout inventoryContainer;

    private ImageView departmentImage;

    private TextView qtyRooms;
    private TextView departmentServices;
    private TextView status;
    private TextView lastMaintance;
    private TextView departmentPrice;
    private TextView type;

    private Button informationBtn;
    private Button inventoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_detail);

        container = findViewById(R.id.container);
        container2 = findViewById(R.id.container2);
        inventoryContainer = findViewById(R.id.inventoryContainer);

        communeGoBack = findViewById(R.id.communeGoBack);
        address = findViewById(R.id.address);
        departmentImage = findViewById(R.id.departmentImageDetail);
        qtyRooms = findViewById(R.id.qtyRooms);
        departmentServices = findViewById(R.id.departmentServices);
        status = findViewById(R.id.status);
        lastMaintance = findViewById(R.id.lastMaintance);
        departmentPrice = findViewById(R.id.departmentPrice);
        type = findViewById(R.id.type);

        informationBtn = findViewById(R.id.informationBtn);
        inventoryBtn = findViewById(R.id.inventoryBtn);

        informationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventoryBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
                informationBtn.setTextColor(Color.parseColor("#FFFFFF"));
                inventoryBtn.setTextColor(Color.parseColor("#0090FF"));
                informationBtn.setBackgroundColor(Color.parseColor("#0090FF"));
                container2.setVisibility(View.INVISIBLE);
                container.setVisibility(View.VISIBLE);
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
                container.setVisibility(View.INVISIBLE);
                getInventory(id);
            }
        });


        getDepartment();
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
                .baseUrl(splashScreen.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DepartmentService departmentService = retrofit.create(DepartmentService.class);
        String jsonData = "{\"department_id\":"+id+"}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
        departmentService.getDepartmentInventory(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(departmentDetail.this, response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    dialog.dismiss();
                    DepartmentInventory[] departmentInventory = new Gson().fromJson(response.body().string(), DepartmentInventory[].class);
                    if(departmentInventory.length == 0){
                        Toast.makeText(departmentDetail.this, "Departamento sin inventario",Toast.LENGTH_SHORT).show();
                        return;
                    };
                    for (DepartmentInventory inventory : departmentInventory){
                        TableLayout titleTableLauout = new TableLayout(departmentDetail.this);
                        TableRow titleBox = new TableRow(departmentDetail.this);
                        TextView spaceEnter = new TextView(departmentDetail.this);

                        spaceEnter.setText("Producto: ");
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(spaceEnter);
                        inventoryContainer.addView(titleTableLauout);

                        TableLayout principalTable = new TableLayout(departmentDetail.this);
                        TableRow containerRow = new TableRow(departmentDetail.this);
                        TableRow containerRow2 = new TableRow(departmentDetail.this);
                        TableRow containerRow3 = new TableRow(departmentDetail.this);
                        TableRow containerRow4 = new TableRow(departmentDetail.this);
                        containerRow.setGravity(Gravity.CENTER);
                        containerRow2.setGravity(Gravity.CENTER);
                        containerRow3.setGravity(Gravity.CENTER);
                        containerRow4.setGravity(Gravity.CENTER);
                        TableRow atributeRow = new TableRow(departmentDetail.this);
                        TableRow atributeRow2 = new TableRow(departmentDetail.this);
                        TableRow atributeRow3 = new TableRow(departmentDetail.this);
                        TableRow atributeRow4 = new TableRow(departmentDetail.this);

                        TableRow spaceRow = new TableRow(departmentDetail.this);
                        TableRow spaceRow2 = new TableRow(departmentDetail.this);
                        TableRow spaceRow3 = new TableRow(departmentDetail.this);
                        TableRow spaceRow4 = new TableRow(departmentDetail.this);

                        TableRow valueRow = new TableRow(departmentDetail.this);
                        TableRow valueRow2 = new TableRow(departmentDetail.this);
                        TableRow valueRow3 = new TableRow(departmentDetail.this);
                        TableRow valueRow4 = new TableRow(departmentDetail.this);

                        TextView atribute1 = new TextView(departmentDetail.this);
                        TextView atribute2 = new TextView(departmentDetail.this);
                        TextView atribute3 = new TextView(departmentDetail.this);
                        TextView atribute4 = new TextView(departmentDetail.this);

                        TextView space = new TextView(departmentDetail.this);

                        TextView value1 = new TextView(departmentDetail.this);
                        TextView value2 = new TextView(departmentDetail.this);
                        TextView value3 = new TextView(departmentDetail.this);
                        TextView value4 = new TextView(departmentDetail.this);

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

            }
        });

    }

    public void getDepartment(){

        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        Integer id = preferences.getInt("id", 0);
        String departmentImageBs64 = preferences.getString("department_image", "NO PHOTO");
        Integer qty_rooms = preferences.getInt("qty_rooms", 0);
        String department_type = preferences.getString("department_type", "SIN REGISTRO");
        Integer price = preferences.getInt("price", 0);
        String commune = preferences.getString("commune", "SIN REGISTRO");
        String departmentAddress = preferences.getString("address", "SIN REGISTRO");

        communeGoBack.setText(commune);
        address.setText(departmentAddress + ", " + commune);
        qtyRooms.setText(qty_rooms.toString());
        byte [] bytes = Base64.decode(departmentImageBs64, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
        departmentImage.setImageBitmap(bitmap);
        departmentPrice.setText(price.toString());
        type.setText(department_type);
    }

    public void extraServicesMenu(View view){
        Intent i = new Intent(this, extraServices.class);
        startActivity(i);
        finish();
    }

    public void goBack(View view){
        SharedPreferences preferences = getSharedPreferences("department", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        finish();
    }

}