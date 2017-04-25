package com.app.maththpt.model;

import io.realm.RealmObject;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class Point extends RealmObject {
    public float point;
    public String time;
    public String userID;
    public int isSynced;

    public Point() {
    }

    public Point(float point, String time, String userID) {
        this.point = point;
        this.time = time;
        this.userID = userID;
    }

}
