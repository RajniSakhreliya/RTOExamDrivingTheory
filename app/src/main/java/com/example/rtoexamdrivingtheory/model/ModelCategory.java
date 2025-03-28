package com.example.rtoexamdrivingtheory.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class ModelCategory implements Serializable {
    int quizNumber;
    Drawable catImage;
    String categoryName;
    int totalQuestions;
    float percentCorrect;

    int ansNoQue;
    int ansYesQue;
    int ansNotAttempted;

    public Drawable getCatImage() {
        return catImage;
    }

    public void setCatImage(Drawable catImage) {
        this.catImage = catImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber(int quizNumber) {
        this.quizNumber = quizNumber;
    }

    public float getPercentCorrect() {
        return percentCorrect;
    }

    public void setPercentCorrect(float percentCorrect) {
        this.percentCorrect = percentCorrect;
    }

    public int getAnsNoQue() {
        return ansNoQue;
    }

    public void setAnsNoQue(int ansNoQue) {
        this.ansNoQue = ansNoQue;
    }

    public int getAnsYesQue() {
        return ansYesQue;
    }

    public void setAnsYesQue(int ansYesQue) {
        this.ansYesQue = ansYesQue;
    }

    public int getAnsNotAttempted() {
        return ansNotAttempted;
    }

    public void setAnsNotAttempted(int ansNotAttempted) {
        this.ansNotAttempted = ansNotAttempted;
    }
}
