package com.example.rtoexamdrivingtheory.model;

import java.io.Serializable;

public class AnswerModel implements Serializable {
    public static int IMAGE = 1;
    String answerText;
    boolean isCorrect;
    int answerType;
    int queId;

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public int getQueId() {
        return queId;
    }

    public void setQueId(int queId) {
        this.queId = queId;
    }
}