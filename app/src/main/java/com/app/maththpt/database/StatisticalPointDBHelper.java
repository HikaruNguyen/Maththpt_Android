package com.app.maththpt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.maththpt.model.StatisticalPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class StatisticalPointDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_STATICTICAL_POINT = "TABLE_STATICTICAL_POINT";
    public static final String STATICTICAL_POINT_ID = "STATICTICAL_POINT_ID";
    public static final String STATICTICAL_POINT_TOTAL_QUESTION = "STATICTICAL_POINT_TOTAL_QUESTION";
    public static final String STATICTICAL_POINT_TOTAL_QUESTION_TRUE = "STATICTICAL_POINT_TOTAL_QUESTION_TRUE";
    public static final String STATICTICAL_POINT_CATEID = "STATICTICAL_POINT_CATEID";
    public static final String STATICTICAL_POINT_CREAT_AT = "STATICTICAL_POINT_CREAT_AT";
    public static final String STATICTICAL_POINT_UPDATE_AT = "STATICTICAL_POINT";

    private static final String DATABASE_NAME = "history";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_STATICTICAL_POINT + "("
            + STATICTICAL_POINT_ID + " text primary key, "
            + STATICTICAL_POINT_TOTAL_QUESTION + " integer,"
            + STATICTICAL_POINT_TOTAL_QUESTION_TRUE + " integer, "
            + STATICTICAL_POINT_CATEID + " integer, "
            + STATICTICAL_POINT_CREAT_AT + " text, "
            + STATICTICAL_POINT_UPDATE_AT + " text );";

    public StatisticalPointDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(StatisticalPointDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATICTICAL_POINT);
        onCreate(db);
    }

    public static class StatisticalPointDatabase {
        private SQLiteDatabase database;
        private StatisticalPointDBHelper dbHelper;
        private String[] allColumns = {
                StatisticalPointDBHelper.STATICTICAL_POINT_ID,
                StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION,
                StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION_TRUE,
                StatisticalPointDBHelper.STATICTICAL_POINT_CATEID,
                StatisticalPointDBHelper.STATICTICAL_POINT_CREAT_AT,
                StatisticalPointDBHelper.STATICTICAL_POINT_UPDATE_AT
        };

        public StatisticalPointDatabase(Context context) {
            dbHelper = new StatisticalPointDBHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        @SuppressWarnings("deprecation")
        public void addAllHistory(List<StatisticalPoint> list) throws Exception {
            database.beginTransaction();
            boolean check = false;
            DatabaseUtils.InsertHelper insertHelper = new DatabaseUtils.InsertHelper(database, StatisticalPointDBHelper.TABLE_STATICTICAL_POINT);

            for (StatisticalPoint obj : list) {
                newAddStaticticalPoint(insertHelper, obj);
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }

        @SuppressWarnings("deprecation")
        public void newAddStaticticalPoint(DatabaseUtils.InsertHelper insertHelper, StatisticalPoint statisticalPoint) {
            if (statisticalPoint == null) {
                return;
            }
            insertHelper.prepareForInsert();
            int column_id = insertHelper
                    .getColumnIndex(StatisticalPointDBHelper.STATICTICAL_POINT_ID);
            int column_total_question = insertHelper
                    .getColumnIndex(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION);
            int column_total_question_true = insertHelper
                    .getColumnIndex(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION_TRUE);
            int column_cateID = insertHelper
                    .getColumnIndex(StatisticalPointDBHelper.STATICTICAL_POINT_CATEID);
            long dtMili = System.currentTimeMillis();
            int column_creat_at = insertHelper
                    .getColumnIndex(StatisticalPointDBHelper.STATICTICAL_POINT_CREAT_AT);
            int column_update_at = insertHelper
                    .getColumnIndex(StatisticalPointDBHelper.STATICTICAL_POINT_UPDATE_AT);
//        insertHelper.bind(column_id, managerDict.getDictName());
            insertHelper.bind(column_total_question, statisticalPoint.getTotalQuestion());
            insertHelper.bind(column_total_question_true, statisticalPoint.getTotalQuestionTrue());
            insertHelper.bind(column_cateID, statisticalPoint.getCateID());
            insertHelper.bind(column_creat_at, dtMili + "");
            insertHelper.bind(column_update_at, dtMili + "");

            insertHelper.execute();
        }

        public boolean addStaticticalPoint(StatisticalPoint statisticalPoint) {
            ContentValues values = new ContentValues();
            long dtMili = System.currentTimeMillis();
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION,
                    statisticalPoint.getTotalQuestion());
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION_TRUE,
                    statisticalPoint.getTotalQuestionTrue());
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_CATEID,
                    statisticalPoint.getCateID());
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_CREAT_AT,
                    dtMili + "");
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_UPDATE_AT,
                    dtMili + "");
            long insertId = database.insert(StatisticalPointDBHelper.TABLE_STATICTICAL_POINT, null,
                    values);

            if (insertId != -1)
                return true;
            else
                return false;
        }


        public boolean deleteAll() {
            int number = database.delete(StatisticalPointDBHelper.TABLE_STATICTICAL_POINT, null, null);
            if (number > 0)
                return true;
            else
                return false;
        }


        public List<StatisticalPoint> getAll() {

            List<StatisticalPoint> statisticalPoints = new ArrayList<StatisticalPoint>();

            Cursor cursor = database.query(StatisticalPointDBHelper.TABLE_STATICTICAL_POINT,
                    allColumns, null, null, null, null,
                    StatisticalPointDBHelper.STATICTICAL_POINT_UPDATE_AT + " DESC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                StatisticalPoint listLiveChannelForm = cursorToLive(cursor);
                statisticalPoints.add(listLiveChannelForm);
                cursor.moveToNext();
            }
            // Make sure to close the cursor
            cursor.close();
            return statisticalPoints;
        }

        public StatisticalPoint getStatisticalPointByCateID(int cateID) {
            String sql = "SELECT * FROM " + StatisticalPointDBHelper.TABLE_STATICTICAL_POINT
                    + " WHERE " + StatisticalPointDBHelper.STATICTICAL_POINT_CATEID + " = " + cateID;
            Cursor cursor = database.rawQuery(sql, null);

            cursor.moveToFirst();
            StatisticalPoint statisticalPoint = cursorToLive(cursor);
            cursor.close();
            return statisticalPoint;
        }

        public boolean isExistCateID(int cateID) {
            Cursor cursor = database.query(StatisticalPointDBHelper.TABLE_STATICTICAL_POINT,
                    allColumns, StatisticalPointDBHelper.STATICTICAL_POINT_CATEID + " = " + cateID, null, null, null, null);
            if (cursor.moveToFirst()) {
                return true;
            } else
                return false;
        }

        public boolean updateStatisticalPointByCateID(int cateID, int total_question, int total_question_true) {
            ContentValues values = new ContentValues();
            long dtMili = System.currentTimeMillis();
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_UPDATE_AT, dtMili + "");
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION, total_question);
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION_TRUE, total_question_true);
            long insertId = database.update(StatisticalPointDBHelper.TABLE_STATICTICAL_POINT, values,
                    StatisticalPointDBHelper.STATICTICAL_POINT_CATEID + " = " + cateID, null);

            if (insertId > 0)
                return true;
            else
                return false;
        }

        public boolean updateStatisticalPointByCateID(StatisticalPoint statisticalPoint) {
            ContentValues values = new ContentValues();
            long dtMili = System.currentTimeMillis();
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_UPDATE_AT, dtMili + "");
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION, statisticalPoint.getTotalQuestion());
            values.put(StatisticalPointDBHelper.STATICTICAL_POINT_TOTAL_QUESTION_TRUE, statisticalPoint.getTotalQuestionTrue());
            long insertId = database.update(StatisticalPointDBHelper.TABLE_STATICTICAL_POINT, values,
                    StatisticalPointDBHelper.STATICTICAL_POINT_CATEID + " = " + statisticalPoint.getCateID(), null);

            if (insertId > 0)
                return true;
            else
                return false;
        }

        private StatisticalPoint cursorToLive(Cursor cursor) {
            return new StatisticalPoint(
                    cursor.getInt(cursor.getColumnIndex(STATICTICAL_POINT_TOTAL_QUESTION_TRUE)),
                    cursor.getInt(cursor.getColumnIndex(STATICTICAL_POINT_TOTAL_QUESTION)),
                    cursor.getInt(cursor.getColumnIndex(STATICTICAL_POINT_CATEID)));
        }


    }
}