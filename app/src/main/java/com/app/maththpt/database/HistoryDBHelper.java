package com.app.maththpt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.maththpt.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 16/02/2017.
 */

public class HistoryDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_HISTORY = "tblhistory";
    private static final String HISTORY_ID = "history_id";
    private static final String HISTORY_POINT = "history_point";
    private static final String HISTORY_CREAT_AT = "history_creat_at";
    private static final String HISTORY_UPDATE_AT = "history_update_at";

    private static final String DATABASE_NAME = "history";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_HISTORY + "("
            + HISTORY_ID + " text primary key, "
            + HISTORY_POINT + " text,"
            + HISTORY_CREAT_AT + " text, "
            + HISTORY_UPDATE_AT + " text );";

    private HistoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HistoryDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public static class HistoryDatabase {
        private SQLiteDatabase database;
        private HistoryDBHelper dbHelper;
        private String[] allColumns = {
                HistoryDBHelper.HISTORY_ID,
                HistoryDBHelper.HISTORY_POINT,
                HistoryDBHelper.HISTORY_CREAT_AT,
                HistoryDBHelper.HISTORY_UPDATE_AT
        };

        public HistoryDatabase(Context context) {
            dbHelper = new HistoryDBHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        @SuppressWarnings("deprecation")
        public void addAllHistory(List<Point> list) throws Exception {
            database.beginTransaction();
            DatabaseUtils.InsertHelper insertHelper =
                    new DatabaseUtils.InsertHelper(database, HistoryDBHelper.TABLE_HISTORY);

            for (Point obj : list) {
                newAddPoint(insertHelper, obj);
            }
            database.setTransactionSuccessful();
            database.endTransaction();
        }

        @SuppressWarnings("deprecation")
        void newAddPoint(DatabaseUtils.InsertHelper insertHelper, Point Point) {
            if (Point == null) {
                return;
            }
            insertHelper.prepareForInsert();
            int column_name = insertHelper
                    .getColumnIndex(HistoryDBHelper.HISTORY_POINT);
            long dtMili = System.currentTimeMillis();
            int column_creat_at = insertHelper
                    .getColumnIndex(HistoryDBHelper.HISTORY_CREAT_AT);
            int column_update_at = insertHelper
                    .getColumnIndex(HistoryDBHelper.HISTORY_UPDATE_AT);
            insertHelper.bind(column_name, Point.point);
            insertHelper.bind(column_creat_at, dtMili + "");
            insertHelper.bind(column_update_at, dtMili + "");

            insertHelper.execute();
        }

        public boolean addPointToHistory(Point point) {
            ContentValues values = new ContentValues();
//        values.put(HistoryDBHelper.MANAGER_DICT_ID,
//                managerDict.getDictName());
            long dtMili = System.currentTimeMillis();
            values.put(HistoryDBHelper.HISTORY_POINT,
                    point.point);
            values.put(HistoryDBHelper.HISTORY_CREAT_AT,
                    dtMili + "");
            values.put(HistoryDBHelper.HISTORY_UPDATE_AT,
                    dtMili + "");
            long insertId = database.insert(HistoryDBHelper.TABLE_HISTORY, null,
                    values);

            return insertId != -1;
        }

        public boolean addPointToHistory(String point) {
            ContentValues values = new ContentValues();
            long dtMili = System.currentTimeMillis();
            values.put(HistoryDBHelper.HISTORY_POINT,
                    point);
            values.put(HistoryDBHelper.HISTORY_CREAT_AT,
                    dtMili + "");
            values.put(HistoryDBHelper.HISTORY_UPDATE_AT,
                    dtMili + "");
            long insertId = database.insert(HistoryDBHelper.TABLE_HISTORY, null,
                    values);
            return insertId != -1;
        }


        public boolean deleteAll() {
            int number = database.delete(HistoryDBHelper.TABLE_HISTORY, null, null);
            return number > 0;
        }


        public List<Point> getAll() {

            List<Point> Points = new ArrayList<Point>();

            Cursor cursor = database.query(HistoryDBHelper.TABLE_HISTORY,
                    allColumns, null, null, null, null,
                    HistoryDBHelper.HISTORY_UPDATE_AT + " DESC");

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Point listLiveChannelForm = cursorToLive(cursor);
                Points.add(listLiveChannelForm);
                cursor.moveToNext();
            }
            // Make sure to close the cursor
            cursor.close();
            return Points;
        }

        public List<Point> getTop10() {

            List<Point> Points = new ArrayList<Point>();

            String sql = "SELECT * FROM " + HistoryDBHelper.TABLE_HISTORY +
                    " ORDER BY " + HistoryDBHelper.HISTORY_CREAT_AT + " DESC lIMIT 10";
            Cursor cursor = database.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Point listLiveChannelForm = cursorToLive(cursor);
                Points.add(listLiveChannelForm);
                cursor.moveToNext();
            }
            cursor.close();
            return Points;
        }

        private Point cursorToLive(Cursor cursor) {
            return new Point(
                    cursor.getString(cursor.getColumnIndex(HISTORY_POINT)),
                    cursor.getString(cursor.getColumnIndex(HISTORY_CREAT_AT)));
        }

        public int getCountHistory() {
            String sql = "SELECT * FROM " + HistoryDBHelper.TABLE_HISTORY;
            Cursor cursor = database.rawQuery(sql, null);
            int count = cursor.getCount();
            cursor.close();
            return count;
        }

        public float getAveragePoint() {
            List<Point> list = getAll();
            if (list == null || list.size() <= 0) {
                return 0;
            } else {
                float sum = 0;
                for (int i = 0; i < list.size(); i++) {
                    try {
                        sum += Float.parseFloat(list.get(i).point);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sum += 0;
                    }
                }
                return sum/list.size();
            }
        }
    }
}