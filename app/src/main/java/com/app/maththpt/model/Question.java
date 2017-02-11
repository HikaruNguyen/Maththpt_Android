package com.app.maththpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by manhi on 11/10/2016.
 */

public class Question implements Serializable, Parcelable {
    public Integer id;
    public String question;
    public String image;
    public List<Answer> answerList;
    public Integer cateID;
    public boolean isCorrect;

    public Question(Integer id, String question, String image, List<Answer> answerList, Integer cateID) {
        this.id = id;
        this.question = question;
        this.image = image;
        this.answerList = answerList;
        this.cateID = cateID;
        isCorrect = false;
    }


    protected Question(Parcel in) {
        question = in.readString();
        image = in.readString();
        answerList = in.createTypedArrayList(Answer.CREATOR);
        isCorrect = in.readByte() != 0;
        id = in.readInt();
        cateID = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(image);
        parcel.writeTypedList(answerList);
        parcel.writeByte((byte) (isCorrect ? 1 : 0));
        parcel.writeInt(id);
        parcel.writeInt(cateID);
    }
}
