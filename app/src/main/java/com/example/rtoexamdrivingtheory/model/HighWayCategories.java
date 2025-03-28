package com.example.rtoexamdrivingtheory.model;

import java.io.Serializable;

public class HighWayCategories implements Serializable {

    private int currentIndex;
    private int id;
    private String name;
    private int percentComplete;
    private int rootCardID;
    private int totalCards;

    public HighWayCategories() {
    }

    public HighWayCategories(int id, String name, int rootCardId, int parentComplete) {
        this.id = id;
        this.name = name;
        this.rootCardID = rootCardId;
        this.percentComplete = parentComplete;
    }


    public int describeContents() {
        return 0;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getPercentComplete() {
        return this.percentComplete;
    }

    public int getRootCardID() {
        return this.rootCardID;
    }

    public int getTotalCards() {
        return this.totalCards;
    }

    public void setCurrentIndex(int i) {
        this.currentIndex = i;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPercentComplete(int i) {
        this.percentComplete = i;
    }

    public void setRootCardID(int i) {
        this.rootCardID = i;
    }

    public void setTotalCards(int i) {
        this.totalCards = i;
    }

}
