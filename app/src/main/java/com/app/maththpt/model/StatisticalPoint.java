package com.app.maththpt.model;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 21/02/2017.
 */

public class StatisticalPoint {
    private int totalQuestionTrue;
    private int totalQuestion;
    private float ratio;

    private int cateID;

    public StatisticalPoint(int totalQuestionTrue, int totalQuestion, int cateID) {
        this.totalQuestionTrue = totalQuestionTrue;
        this.totalQuestion = totalQuestion;
        this.cateID = cateID;
        if (totalQuestion <= 0) {
            ratio = -1;
        } else {
            ratio = totalQuestionTrue / totalQuestion;
        }
    }

    public int getCateID() {
        return cateID;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getTotalQuestionTrue() {
        return totalQuestionTrue;
    }

    public void setTotalQuestionTrue(int totalQuestionTrue) {
        this.totalQuestionTrue = totalQuestionTrue;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }
}
