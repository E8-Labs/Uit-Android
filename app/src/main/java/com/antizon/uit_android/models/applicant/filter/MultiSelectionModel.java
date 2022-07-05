package com.antizon.uit_android.models.applicant.filter;

public class MultiSelectionModel {
    String title;
    int value;
    boolean isSelected;

    public MultiSelectionModel(String title, int value) {
        this.title = title;
        this.value = value;
    }

    public MultiSelectionModel(String title, int value, boolean isSelected) {
        this.title = title;
        this.value = value;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
