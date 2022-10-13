package com.example.turismoreal.administrator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.turismoreal.R;
import com.example.turismoreal.Services.CommuneService;
import com.example.turismoreal.Services.DepartmentService;
import com.example.turismoreal.models.Commune;
import com.example.turismoreal.models.OneResponse;
import com.example.turismoreal.SplashScreen;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDepartment extends AppCompatActivity {

    private Switch switchButton;
    private Spinner communeSpinner;
    private Spinner departmentTypeSpinner;


    private EditText address;
    private EditText qtyRoom;
    private EditText price;
    private EditText shortDescription;
    private EditText longDescription;
    private ImageView departmentImage;
    private EditText base64String;
    private Uri imagePath;
    private String base64ImageString;


    private Integer department_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_TurismoReal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);

        address = findViewById(R.id.departmentDirection);
        switchButton = findViewById(R.id.switchAvaible);
        qtyRoom = findViewById(R.id.qtyRoom);
        price = findViewById(R.id.price);
        communeSpinner = findViewById(R.id.communeSpinner);
        departmentTypeSpinner = findViewById(R.id.departmentTypeSpinner);
        shortDescription = findViewById(R.id.shortDescription);
        longDescription = findViewById(R.id.longDescription);
        base64String = findViewById(R.id.base64Image);
        departmentImage = (ImageView) findViewById(R.id.departmentImage);

        String [] departmentType = {"Selecciona un tipo de departamento","Loft", "Estudio", "Pareja", "Familiar", "Penthouse"};

        ArrayAdapter<String> departmentTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, departmentType);

        departmentTypeSpinner.setAdapter(departmentTypeAdapter);

        try{
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SplashScreen.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CommuneService communeService = retrofit.create(CommuneService.class);
            Call<List<Commune>> allCommunes = communeService.getCommunes();
            allCommunes.enqueue(new Callback<List<Commune>>() {
                @Override
                public void onResponse(Call<List<Commune>> call, Response<List<Commune>> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(AddDepartment.this, response.code(), Toast.LENGTH_LONG);
                        return;
                    }
                    List<Commune>communes = response.body();
                    ArrayList<String> options = new ArrayList<String>();
                    options.add("Seccionar una comuna");
                    for (Commune i : communes){
                            options.add(i.getCommune());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddDepartment.this, android.R.layout.simple_spinner_item, options);
                    communeSpinner.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Commune>> call, Throwable t) {

                }
            });


        }catch (Exception e){

        }


    }

    public void loadImage(View view){
       image();
    }

    public void image(){
        Intent loadImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        loadImage.setType("image/");
        startActivityForResult(loadImage.createChooser(loadImage,"Seleccione una aplicación"),10);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            Uri path = data.getData();
            departmentImage.setImageURI(path);
            imagePath = path;
        }
    }

    public void addDepartment(View view){

        String departmentCommune = communeSpinner.getSelectedItem().toString();
        String departmentType = departmentTypeSpinner.getSelectedItem().toString();
        if (address.getText().toString().isEmpty()){
            Toast.makeText(AddDepartment.this,"Debe agregar la dirección del departamento", Toast.LENGTH_SHORT).show();
        }else if(qtyRoom.getText().toString().isEmpty()){
            Toast.makeText(AddDepartment.this,"Debe indicar la cantidad de piezas", Toast.LENGTH_SHORT).show();
        }else if(price.getText().toString().isEmpty()){
            Toast.makeText(AddDepartment.this,"Debe indicar el precio del departamento", Toast.LENGTH_SHORT).show();
        }else if (departmentCommune.equals("Seccionar una comuna")){
            Toast.makeText(AddDepartment.this,"Debe seleccionar una comuna", Toast.LENGTH_SHORT).show();
        }else if(departmentType.equals("Selecciona un tipo de departamento")){
            Toast.makeText(AddDepartment.this,"Debe seleccionar un tipo de departamento", Toast.LENGTH_SHORT).show();
        }else if(shortDescription.getText().toString().isEmpty()){
            Toast.makeText(AddDepartment.this,"Debe indicar una descripción corta", Toast.LENGTH_SHORT).show();
        }else if (longDescription.getText().toString().isEmpty()){
            Toast.makeText(AddDepartment.this,"Debe indicar una descripcin larga", Toast.LENGTH_SHORT).show();
        }else{
            String departmetAddress = address.getText().toString();
            Integer departmentQtyRoom = Integer.parseInt(qtyRoom.getText().toString());
            Integer departmentPrice = Integer.parseInt(price.getText().toString());
            String sDescription = shortDescription.getText().toString();
            String lDescription = longDescription.getText().toString();
            try {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] bytes = stream.toByteArray();
                    base64ImageString = Base64.encodeToString(bytes, Base64.NO_WRAP);

                    base64String.setText(base64ImageString);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String base64Image = base64String.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SplashScreen.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                DepartmentService departmentService = retrofit.create(DepartmentService.class);
                String jsonData = "{\"address\":\""+departmetAddress+"\",\"qty_rooms\":"+departmentQtyRoom+",\"price\":"+departmentPrice+",\"commune\":\""+departmentCommune+"\",\"department_type\":\""+departmentType+"\",\"short_description\":\""+sDescription+"\",\"long_description\":\""+lDescription+"\",\"department_image\":\""+base64Image+"\"}";
                RequestBody requestBody = RequestBody.create(MediaType.parse("aplicaton/json"), jsonData);
                departmentService.addDepartment(requestBody).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()){
                            Toast.makeText(AddDepartment.this, "Error al agregar el departamento", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            Gson g = new Gson();
                            OneResponse post_response = g.fromJson(response.body().string(), OneResponse.class);
                            department_id = post_response.getResponse();
                            if (department_id != 0) {
                                Toast.makeText(AddDepartment.this, "Departamento Agregado correctamente", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AddDepartment.this, DepartmentPage.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(AddDepartment.this, "Error al agregar el departamento", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }catch (Exception e){
            }
        }
    }

    public void goBack(View view){
        Intent i = new Intent(this, DepartmentPage.class);
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