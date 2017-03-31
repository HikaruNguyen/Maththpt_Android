package com.app.maththpt.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 31/03/2017.
 */

public class CacheCategory extends RealmObject {
    @PrimaryKey
    public int cateID;
    public String json;

    public CacheCategory() {

    }

    public CacheCategory(int testID, String json) {
        this.cateID = testID;
        this.json = json;
    }
}
