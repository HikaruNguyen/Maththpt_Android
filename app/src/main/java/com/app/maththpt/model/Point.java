package com.app.maththpt.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class Point extends RealmObject implements Serializable {
    @PrimaryKey
    public int id;
    public float point;
    public String time;
    public String userID;
    public int numQuestion;
    public String timeQuestion;
    public String listCate;
    public int totalQuestionTrue;
    public int isSynced;
    @Ignore
    public List<DetailPoint> listdetailPoints;
    public String detailPoint;

    public Point() {
    }

//    public Point(float point, String time, String userID) {
//        this.point = point;
//        this.time = time;
//        this.userID = userID;
//    }


    public Point(int id, float point, String time, String userID, int numQuestion,
                 String timeQuestion, String listCate, int totalQuestionTrue, int isSynced) {
        this.id = id;
        this.point = point;
        this.time = time;
        this.userID = userID;
        this.numQuestion = numQuestion;
        this.timeQuestion = timeQuestion;
        this.listCate = listCate;
        this.totalQuestionTrue = totalQuestionTrue;
        this.isSynced = isSynced;
    }

    public String getParamSync() {
        return point + "+" + time + "+" + userID + "+" + numQuestion + "+"
                + timeQuestion + "+" + listCate + "+" + totalQuestionTrue;
    }

    public String getDetailPoint(List<DetailPoint> listdetailPoints) {
        Gson gson = new Gson();
        if (listdetailPoints != null && listdetailPoints.size() > 0) {
            return gson.toJson(listdetailPoints);
        } else {
            return "";
        }
    }

    public List<DetailPoint> getListdetailPoints(String detailPoint) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<DetailPoint>>() {
        }.getType();
        return gson.fromJson(detailPoint, type);
    }
}
