package com.example.turismoreal.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Department {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("is_new")
    @Expose
    private Integer isNew;
    @SerializedName("qty_room")
    @Expose
    private int qtyRoom;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("commune")
    @Expose
    private String commune;
    @SerializedName("department_type")
    @Expose
    private String departmentType;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("long_description")
    @Expose
    private String longDescription;
    @SerializedName("department_image")
    @Expose
    private String departmentImage;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getQtyRoom() {
        return qtyRoom;
    }

    public void setQtyRoom(int qtyRoom) {
        this.qtyRoom = qtyRoom;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {this.shortDescription = shortDescription;}

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {this.longDescription = longDescription;}

    public String getDepartmentImage() {return departmentImage;}

    public void setDepartmentImage(String departmentImage) {this.departmentImage = departmentImage;}

    @Override
    public String toString() {
        return "Department{" +
                "address='" + address + '\'' +
                ", qtyRoom=" + qtyRoom +
                ", price=" + price +
                ", commune='" + commune + '\'' +
                ", departmentType='" + departmentType + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", departmentImage='" + departmentImage + '\'' +
                '}';
    }
}
