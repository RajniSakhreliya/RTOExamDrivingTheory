package com.example.rtoexamdrivingtheory.model;

import java.io.Serializable;

public class FlagModel implements Serializable {
    String time;

    public FlagModel(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
