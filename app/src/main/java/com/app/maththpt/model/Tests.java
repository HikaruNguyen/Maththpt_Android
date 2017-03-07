package com.app.maththpt.model;

import android.graphics.Typeface;
import android.widget.TextView;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class Tests extends RealmObject {
    @PrimaryKey
    public int id;
    public String displayname;
    public String author;
    @Ignore
    public boolean isNew;
    public boolean isSeen;

    public Tests() {
        isSeen = false;
    }

    public Tests(int id, String name, String author) {
        this.id = id;
        this.displayname = name;
        this.author = author;
        isSeen = false;
    }

    public Tests(String name, String author) {
        this.displayname = name;
        this.author = author;
        isSeen = false;
    }

    @android.databinding.BindingAdapter("android:typeface")
    public static void setTypeface(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }
}
