package com.antizon.uit_android.generic.model;

import java.io.Serializable;

public class ModelApplicantDegree implements Serializable {

    int id;
    String name;
    String subTitle;
    Boolean isSelected = false;
    public ModelApplicantDegree() {
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }
}
