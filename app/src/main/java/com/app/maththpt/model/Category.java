package com.app.maththpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by manhi on 3/1/2017.
 */

public class Category extends RealmObject implements Parcelable {
    @PrimaryKey
    public int id;
    public String name;
    public int icon;
    @Ignore
    public boolean isChecked;
    @SerializedName("countQuestion")
    public int countTotalQuestion;
    public int countViewQuestion;

    public Category() {
        isChecked = true;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
        isChecked = true;
    }

    public Category(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;

    }

    public Category(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.isChecked = true;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        icon = in.readInt();
        countTotalQuestion = in.readInt();
        countViewQuestion = in.readInt();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(icon);
        parcel.writeInt(countTotalQuestion);
        parcel.writeInt(countViewQuestion);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
