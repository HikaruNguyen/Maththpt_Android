package com.app.maththpt.realm;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 25/04/2017.
 */

public class HistoryMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            RealmObjectSchema recipeSchema = schema.get("Point");
            recipeSchema.addField("isSynced", int.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setInt("isSynced", 0);
                        }
                    });
            oldVersion++;
        }
        if (oldVersion == 1) {
            RealmObjectSchema recipeSchema = schema.get("Point");
            recipeSchema.addField("id", int.class, FieldAttribute.PRIMARY_KEY)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setInt("isSynced", 0);
                        }
                    });
            oldVersion++;
        }
    }
}
