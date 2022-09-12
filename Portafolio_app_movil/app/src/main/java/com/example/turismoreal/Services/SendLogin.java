package com.example.turismoreal.Services;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendLogin {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("error")
    @Expose
    private String error;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}