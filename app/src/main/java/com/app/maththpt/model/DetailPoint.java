package com.app.maththpt.model;

import java.io.Serializable;

/**
 * Created by manhi on 6/1/2017.
 */

public class DetailPoint implements Serializable {
    public String name;
    public int trueQuestion;
    public int sumQuestion;

    public DetailPoint() {
    }

    public DetailPoint(String name, int sumQuestion, int trueQuestion) {
        this.name = name;
        this.sumQuestion = sumQuestion;
        this.trueQuestion = trueQuestion;
    }

    public float getRatio() {
        float ratio;
        if (sumQuestion <= 0) {
            ratio = -1;
        } else {
            ratio = ((float) trueQuestion * 100) / sumQuestion;
        }
        return ratio;
    }
}
