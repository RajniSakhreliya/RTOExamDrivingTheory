package com.example.rtoexamdrivingtheory.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelTrafficCategory implements Serializable {
    String type;
    ArrayList<ModelTraffic> list = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ModelTraffic> getList() {
        return list;
    }

    public void setList(ArrayList<ModelTraffic> list) {
        this.list = list;
    }
}
