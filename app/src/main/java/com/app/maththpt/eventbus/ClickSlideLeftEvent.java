package com.app.maththpt.eventbus;

/**
 * Created by manhi on 7/6/2016.
 */

public class ClickSlideLeftEvent {
    public int position;
    public boolean isChange;
    public String id;
    public int type;

    public ClickSlideLeftEvent(int position, boolean isChange, int type) {
        this.position = position;
        this.isChange = isChange;
        this.type = type;
    }

    public ClickSlideLeftEvent(int position, String id, boolean isChange, int type) {
        this.position = position;
        this.isChange = isChange;
        this.type = type;
        this.id = id;
    }
}
