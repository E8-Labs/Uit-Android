package com.antizon.uit_android.generic.model;

public class GenericModel
{
    int id;
    String name;

    public GenericModel() {
    }

    public GenericModel(int id, String name) {
        this.id = id;
        this.name = name;
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
