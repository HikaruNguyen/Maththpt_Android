package com.app.maththpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by manhi on 12/10/2016.
 */

public class Answer implements Serializable, Parcelable {
    public String answer;
    public boolean isCorrect;
//    public int type;

    public Answer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
//        this.type = type;
    }

    public Answer() {
        this.isCorrect = false;
    }

    protected Answer(Parcel in) {
        answer = in.readString();
        isCorrect = in.readByte() != 0;
//        type = in.readInt();
    }

    public static final Creator<Answer> CREATOR = new Creator<Answer>() {
        @Override
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        @Override
        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(answer);
        parcel.writeByte((byte) (isCorrect ? 1 : 0));
//        parcel.writeInt(type);
    }
}
