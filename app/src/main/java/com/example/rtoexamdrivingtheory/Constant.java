package com.example.rtoexamdrivingtheory;

import android.graphics.Color;

import java.util.ArrayList;

public class Constant {

    public static ArrayList<Integer> listColorConstants = new ArrayList<>();

    public static int SELECT_QUESTION_ACTIVITY = 100;
    public static int HIGHWAY_CODE_DES_ACTIVITY = 110;

    static {
        listColorConstants.add(Color.parseColor("#F44336"));
        listColorConstants.add(Color.parseColor("#E91E63"));
        listColorConstants.add(Color.parseColor("#9C27B0"));
        listColorConstants.add(Color.parseColor("#673AB7"));
        listColorConstants.add(Color.parseColor("#3F51B5"));
        listColorConstants.add(Color.parseColor("#2196F3"));
        listColorConstants.add(Color.parseColor("#03A9F4"));
        listColorConstants.add(Color.parseColor("#00BCD4"));
        listColorConstants.add(Color.parseColor("#009688"));
        listColorConstants.add(Color.parseColor("#4CAF50"));
        listColorConstants.add(Color.parseColor("#8BC34A"));
        listColorConstants.add(Color.parseColor("#CDDC39"));
        listColorConstants.add(Color.parseColor("#FFEB3B"));
        listColorConstants.add(Color.parseColor("#FFC107"));
        listColorConstants.add(Color.parseColor("#FF9800"));
        listColorConstants.add(Color.parseColor("#FF5722"));
    }

    public static ArrayList<Integer> getColorList(int size) {
        ArrayList<Integer> colorList = new ArrayList<>();
        int colorPos = 0;
        for (int i = 0; i < size; i++) {
            colorList.add(Constant.listColorConstants.get(colorPos));
            colorPos++;
            if (colorPos >= Constant.listColorConstants.size()) {
                colorPos = 0;
            }
        }
        return colorList;
    }


}
