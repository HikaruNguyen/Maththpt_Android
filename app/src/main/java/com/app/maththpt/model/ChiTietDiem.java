package com.app.maththpt.model;

/**
 * Created by manhi on 6/1/2017.
 */

public class ChiTietDiem {
    public String name;
    public int trueQuestion;
    public int sumQuestion;

    public ChiTietDiem() {
    }

    public ChiTietDiem(String name, int sumQuestion, int trueQuestion) {
        this.name = name;
        this.sumQuestion = sumQuestion;
        this.trueQuestion = trueQuestion;
    }
}
