package com.example.bitalinomonitor.commands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CreatePatientCommand {
    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("Phone")
    @Expose
    public String phone;

    @SerializedName("PhotoPath")
    @Expose
    public String photoPath;

    @SerializedName("DateOfBirth")
    @Expose
    public Date dateOfBirth;
}
