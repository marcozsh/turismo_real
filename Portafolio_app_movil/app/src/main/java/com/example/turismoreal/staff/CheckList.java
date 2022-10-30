package com.example.turismoreal.staff;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.SplashScreen;
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

public class CheckList extends AppCompatActivity {

    private LinearLayout linearLayout;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        linearLayout = findViewById(R.id.linearLayout);
        SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Integer id = preferences.getInt("department_id", 0);
        getInventory(id);
    }


    public void getInventory(Integer id) {
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
        String jsonData = "{\"department_id\":" + id + "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
        departmentService.getDepartmentInventory(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    dialog.dismiss();
                    DepartmentInventory[] departmentInventory = new Gson().fromJson(response.body().string(), DepartmentInventory[].class);
                    for (DepartmentInventory inventory : departmentInventory) {
                        TableLayout titleTableLauout = new TableLayout(CheckList.this);
                        TableRow titleBox = new TableRow(CheckList.this);
                        TextView spaceEnter = new TextView(CheckList.this);

                        spaceEnter.setText("Producto: ");
                        spaceEnter.setTextColor(Color.parseColor("#0090FF"));
                        titleTableLauout.addView(titleBox);
                        titleBox.addView(spaceEnter);
                        linearLayout.addView(titleTableLauout);

                        TableLayout principalTable = new TableLayout(CheckList.this);
                        TableRow containerRow = new TableRow(CheckList.this);
                        TableRow containerRow2 = new TableRow(CheckList.this);
                        TableRow containerRow3 = new TableRow(CheckList.this);
                        TableRow containerRow4 = new TableRow(CheckList.this);
                        TableRow containerRow5 = new TableRow(CheckList.this);
                        containerRow.setGravity(Gravity.LEFT);
                        containerRow2.setGravity(Gravity.LEFT);
                        containerRow3.setGravity(Gravity.LEFT);
                        containerRow4.setGravity(Gravity.LEFT);
                        containerRow5.setGravity(Gravity.LEFT);
                        TableRow atributeRow = new TableRow(CheckList.this);
                        TableRow atributeRow2 = new TableRow(CheckList.this);
                        TableRow atributeRow3 = new TableRow(CheckList.this);
                        TableRow atributeRow4 = new TableRow(CheckList.this);
                        TableRow atributeRow5 = new TableRow(CheckList.this);

                        TableRow spaceRow = new TableRow(CheckList.this);
                        TableRow spaceRow2 = new TableRow(CheckList.this);
                        TableRow spaceRow3 = new TableRow(CheckList.this);
                        TableRow spaceRow4 = new TableRow(CheckList.this);
                        TableRow spaceRow5 = new TableRow(CheckList.this);

                        TableRow valueRow = new TableRow(CheckList.this);
                        TableRow valueRow2 = new TableRow(CheckList.this);
                        TableRow valueRow3 = new TableRow(CheckList.this);
                        TableRow valueRow4 = new TableRow(CheckList.this);
                        TableRow valueRow5 = new TableRow(CheckList.this);

                        TextView atribute1 = new TextView(CheckList.this);
                        TextView atribute2 = new TextView(CheckList.this);
                        TextView atribute3 = new TextView(CheckList.this);
                        TextView atribute4 = new TextView(CheckList.this);
                        TextView atribute5 = new TextView(CheckList.this);

                        TextView space = new TextView(CheckList.this);

                        TextView value1 = new TextView(CheckList.this);
                        TextView value2 = new TextView(CheckList.this);
                        TextView value3 = new TextView(CheckList.this);
                        TextView value4 = new TextView(CheckList.this);
                        TextView value5 = new TextView(CheckList.this);

                        atribute1.setText("Nombre");
                        atribute2.setText("Marca");
                        atribute3.setText("Tipo de producto");
                        atribute4.setText("Cantidad");
                        atribute5.setText("Price");

                        space.setText("      ");

                        value1.setTextColor(Color.parseColor("#000000"));
                        value2.setTextColor(Color.parseColor("#000000"));
                        value3.setTextColor(Color.parseColor("#000000"));
                        value4.setTextColor(Color.parseColor("#000000"));
                        value5.setTextColor(Color.parseColor("#000000"));

                        String name = inventory.getName();
                        value1.setText(name);
                        String brand = inventory.getBrand();
                        value2.setText(brand);
                        String productType = inventory.getProductType();
                        value3.setText(productType);
                        Integer qty = inventory.getQty();
                        value4.setText(qty.toString());
                        Integer price = inventory.getPrice();
                        value5.setText("$ "+price);

                        linearLayout.addView(principalTable);

                        principalTable.addView(containerRow);
                        principalTable.addView(containerRow2);
                        principalTable.addView(containerRow3);
                        principalTable.addView(containerRow4);
                        principalTable.addView(containerRow5);

                        containerRow.addView(atributeRow);
                        containerRow2.addView(atributeRow2);
                        containerRow3.addView(atributeRow3);
                        containerRow4.addView(atributeRow4);
                        containerRow5.addView(atributeRow5);

                        containerRow.addView(spaceRow);
                        containerRow2.addView(spaceRow2);
                        containerRow3.addView(spaceRow3);
                        containerRow4.addView(spaceRow4);
                        containerRow5.addView(spaceRow5);

                        containerRow.addView(valueRow);
                        containerRow2.addView(valueRow2);
                        containerRow3.addView(valueRow3);
                        containerRow4.addView(valueRow4);
                        containerRow5.addView(valueRow5);

                        atributeRow.addView(atribute1);
                        atributeRow2.addView(atribute2);
                        atributeRow3.addView(atribute3);
                        atributeRow4.addView(atribute4);
                        atributeRow5.addView(atribute5);

                        valueRow.addView(value1);
                        valueRow2.addView(value2);
                        valueRow3.addView(value3);
                        valueRow4.addView(value4);
                        valueRow5.addView(value5);
                        spaceRow.addView(space);

                        principalTable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences preferences = getSharedPreferences("product_status", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("product_id",inventory.getProduct_id());
                                editor.commit();
                                Intent i = new Intent(CheckList.this, InventoryList.class);
                                startActivity(i);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    public void checkOut(View view){
        SharedPreferences preferences = getSharedPreferences("reservation_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Integer idReservation = preferences.getInt("reservationId", 0);
        String mailCustomer = preferences.getString("email", "none");
        if(idReservation != 0 && !mailCustomer.equals("none")){
            String subject = "Confirmación Check Out(RESERVA \""+idReservation+"\")";
            String body = "http://marcozsh/turismo_real/check_out/?reservation_id="+idReservation;
            Intent selectionIntent = new Intent(Intent.ACTION_SENDTO);
            selectionIntent.setData(Uri.parse("mailto:"+mailCustomer+"?subject="+Uri.encode(subject)+"&body="+Uri.encode(body)));
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setSelector(selectionIntent);
            startActivity(Intent.createChooser(i, "Select An App"));
        }else{
            Toast.makeText(this, "A ocurrido un error, intentelo más tarde", Toast.LENGTH_SHORT).show();
        }
    }

    public void goCheckOut(View view){
        Intent i = new Intent(this, CheckOut.class);
        startActivity(i);
        finish();
    }
}