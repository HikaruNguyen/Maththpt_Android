package com.app.maththpt.eventbus;

/**
 * Created by manhi on 4/1/2017.
 */

public class CheckAnswerQuestionEvent {
    //Check dap an dung
    public static final int TYPE_CHECK = 1;
    //Xem dap an chi tiet
    public static final int TYPE_DETAIL = 2;
    public int position;
    public int type;
    public boolean isCheckAll;

    public CheckAnswerQuestionEvent(int position, int type) {
        this.position = position;
        this.type = type;
        isCheckAll = false;
    }

    public CheckAnswerQuestionEvent() {
        isCheckAll = true;
        this.type = TYPE_CHECK;
    }
}
