package com.app.maththpt.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 31/03/2017.
 */

public class CacheTests extends RealmObject {
    @PrimaryKey
    public String testID;
    public String json;

    public CacheTests() {

    }

    public CacheTests(String testID, String json) {
        this.testID = testID;
        this.json = json;
    }
}
