package com.example.bitalinomonitor.models;

public class MainOptionModel {
    private String title;
    private String description;
    private int image;
    private Class<?> intentClass;

    public MainOptionModel(String title, String description, int image, Class<?> intentClass){
        this.title = title;
        this.description = description;
        this.image = image;
        this.intentClass = intentClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Class<?> getIntentClass() {
        return intentClass;
    }

    public void setIntentClass(Class<?> intentClass) {
        this.intentClass = intentClass;
    }
}
