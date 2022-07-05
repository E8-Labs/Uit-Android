package com.antizon.uit_android.generic.model;

import java.io.Serializable;

public class ModelChannelList implements Serializable {

    int id;
    String name="",image_path="";

    public ModelChannelList() {
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
