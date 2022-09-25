package com.example.turismoreal.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commune {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("commune")
    @Expose
    private String commune;
    @SerializedName("id_region_id")
    @Expose
    private int idRegionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public int getIdRegionId() {
        return idRegionId;
    }

    public void setIdRegionId(int idRegionId) {
        this.idRegionId = idRegionId;
    }

}