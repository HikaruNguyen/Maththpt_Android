package com.app.maththpt.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.maththpt.model.Category;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by manhi on 5/1/5016.
 */
public class CategoryDBHelper {

    public static final String TAG = CategoryDBHelper.class.getSimpleName();
    private static final String CategoryID = "id";
    private static final String Name = "name";
    private static final String DB_NAME = "de_c01_001.sqlite";
    private static final String TABLE_Question = "tbl_Category";

    public static List<Category> getAllListCategory(Context context) {
        List<Category> list = new ArrayList<>();
        AssetDatabaseOpenHelper adb = new AssetDatabaseOpenHelper(context);
        SQLiteDatabase database = adb.StoreDatabase(DB_NAME);
        Cursor cursor;
        String sql = "";
        sql += "SELECT * FROM " + TABLE_Question;
        if (database != null) {
            cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(getQuestionByCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }


    private static Category getQuestionByCursor(Cursor cursor) {
        Integer id = cursor.getInt(cursor.getColumnIndex(CategoryID));
        String name = cursor.getString(cursor.getColumnIndex(Name));
        return new Category(
                id,
                name);
    }
}
