package com.example.turismoreal.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("rut")
    @Expose
    private String rut;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("rol")
    @Expose
    private String rol;

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}