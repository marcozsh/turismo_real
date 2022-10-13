package com.example.turismoreal.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartmentDisponibility {

    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("maintenance_start")
    @Expose
    private String maintenanceStart;
    @SerializedName("maintenance_finish")
    @Expose
    private String maintenanceFinish;

    public String getCheckIn() {
        return checkIn;
    }

    public String getMaintenanceStart() {
        return maintenanceStart;
    }

    public void setMaintenanceStart(String maintenanceStart) {
        this.maintenanceStart = maintenanceStart;
    }

    public String getMaintenanceFinish() {
        return maintenanceFinish;
    }

    public void setMaintenanceFinish(String maintenanceFinish) {
        this.maintenanceFinish = maintenanceFinish;
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



}