package com.antizon.uit_android.generic.model;

import java.io.Serializable;

public class ModelApplicantEducation implements Serializable {

    int id;
    String name,subTitle;


    public ModelApplicantEducation() {
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

    public void setName(String name) {
        this.name = name;
    }
}
