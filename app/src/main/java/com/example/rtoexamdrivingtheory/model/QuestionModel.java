package com.example.rtoexamdrivingtheory.model;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionModel implements Serializable {
    int questionId;
    String questionTitle, questionExplanation, questionImage;
    ArrayList<AnswerModel> answerList = new ArrayList<>();

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionExplanation() {
        return questionExplanation;
    }

    public void setQuestionExplanation(String questionExplanation) {
        this.questionExplanation = questionExplanation;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public ArrayList<AnswerModel> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(ArrayList<AnswerModel> answerList) {
        this.answerList = answerList;
    }

}
