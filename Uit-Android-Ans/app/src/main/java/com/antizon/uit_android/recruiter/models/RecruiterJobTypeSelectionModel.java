package com.antizon.uit_android.recruiter.models;

public class RecruiterJobTypeSelectionModel {
    private String id, name, image;
    boolean isSelected;


    public RecruiterJobTypeSelectionModel(String id, String name, String image, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
