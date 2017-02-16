package com.app.maththpt.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class Tests {
    public int id;
    public String displayname;
    public String author;

    public Tests(int id, String name, String author) {
        this.id = id;
        this.displayname = name;
        this.author = author;
    }

    public Tests(String name, String author) {
        this.displayname = name;
        this.author = author;
    }
}
