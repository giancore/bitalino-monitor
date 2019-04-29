package com.example.bitalinomonitor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class PatientModel implements Serializable {
    @SerializedName("Id")
    @Expose
    private UUID id;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Phone")
    @Expose
    private String telephone;

    @SerializedName("PhotoPath")
    @Expose
    private String photoPath;

    @SerializedName("DateOfBirth")
    @Expose
    private Date dateOfBirth;

    @SerializedName("Exams")
    @Expose
    private ArrayList<ExamModel> exams;

    public PatientModel() {}

    public PatientModel(UUID id, String name, String phone, String photoPath){
        this.id = id;
        this.name = name;
        this.telephone = phone;
        this.photoPath = photoPath;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfBirthAsString() {
        return new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR")).format(this.dateOfBirth);
    }

    public ArrayList<ExamModel> getExams() {
        return exams;
    }

    public void setExams(ArrayList<ExamModel> exams) {
        this.exams = exams;
    }
}
