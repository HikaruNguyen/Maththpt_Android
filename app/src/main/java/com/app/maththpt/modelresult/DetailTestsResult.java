package com.app.maththpt.modelresult;

import java.util.List;

/**
 * Created by manhi on 16/2/2017.
 */

public class DetailTestsResult extends BaseResult {
    public List<Question> data;

    public static class Question {
        public String id;
        public String testID;
        public String cateID;
        public String question;
        public String image;
        public String answerA;
        public String answerB;
        public String answerC;
        public String answerD;
        public String answerTrue;
        public String answerDetail;
    }
}
