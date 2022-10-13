package com.example.turismoreal.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("total_amount")
    @Expose
    private int totalAmount;
    @SerializedName("reservation_amount")
    @Expose
    private int reservationAmount;
    @SerializedName("qty_customers")
    @Expose
    private int qtyCustomers;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;



    @SerializedName("department")
    @Expose
    private List<Department> department = null;
    @SerializedName("service_extra")
    @Expose
    private List<ExtraService> serviceExtra = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getReservationAmount() {
        return reservationAmount;
    }

    public void setReservationAmount(int reservationAmount) {
        this.reservationAmount = reservationAmount;
    }

    public int getQtyCustomers() {
        return qtyCustomers;
    }

    public void setQtyCustomers(int qtyCustomers) {
        this.qtyCustomers = qtyCustomers;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Department> getDepartment() {
        return department;
    }

    public void setDepartment(List<Department> department) {
        this.department = department;
    }

    public List<ExtraService> getServiceExtra() {
        return serviceExtra;
    }

    public void setServiceExtra(List<ExtraService> serviceExtra) {
        this.serviceExtra = serviceExtra;
    }

}