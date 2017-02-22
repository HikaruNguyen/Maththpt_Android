package com.app.maththpt.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.maththpt.model.Answer;
import com.app.maththpt.model.Category;
import com.app.maththpt.model.Question;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by manhi on 5/1/5016.
 */
public class QuestionDBHelper {

    public static final String TAG = QuestionDBHelper.class.getSimpleName();
    public static final String QuestionID = "id";
    private static final String Question = "question";
    private static final String Image = "image";
    private static final String Answer1 = "answerA";
    private static final String Answer2 = "answerB";
    private static final String Answer3 = "answerC";
    private static final String Answer4 = "answerD";
    private static final String AnswerTrue = "answerTrue";
    private static final String CategoryID = "CateID";
//    private static final String TypeA = "typeA";
//    private static final String TypeB = "typeB";
//    private static final String TypeC = "typeC";
//    private static final String TypeD = "typeD";
//    private static final String TypeQuestion = "typeQuestion";

    public static final String DB_NAME = "de_c01_001.sqlite";
    public static final String TABLE_Question = "tbl_question";

    public static List<Question> getAllListQuestion(Context context) {
        List<Question> list = new ArrayList<>();
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

    public static List<Question> getListQuestionByCateID(Context context, int cateID) {
        List<Question> list = new ArrayList<>();
        AssetDatabaseOpenHelper adb = new AssetDatabaseOpenHelper(context);
        SQLiteDatabase database = adb.StoreDatabase(DB_NAME);
        Cursor cursor;
        String sql = "";
        sql += "SELECT * FROM " + TABLE_Question;
        sql += " WHERE " + CategoryID + " = " + cateID;
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

    public static List<Question> getListQuestionByListCateID(
            Context context, List<Category> listCate, int soCau) {
        List<Question> list = new ArrayList<>();
        AssetDatabaseOpenHelper adb = new AssetDatabaseOpenHelper(context);
        SQLiteDatabase database = adb.StoreDatabase(DB_NAME);
        Cursor cursor;
        String sql = "";
        sql += "SELECT * FROM " + TABLE_Question;
        if (listCate != null) {
            if (listCate.size() > 0) {
                sql += " WHERE " + CategoryID + " = " + listCate.get(0).id;
            }
            if (listCate.size() > 1) {
                for (int i = 1; i < listCate.size(); i++) {
                    sql += " OR " + CategoryID + " = " + listCate.get(i).id;
                }
            }
            sql += " ORDER BY RANDOM() LIMIT " + soCau;
        }

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

    private static Question getQuestionByCursor(Cursor cursor) {
        Integer id = cursor.getInt(cursor.getColumnIndex(QuestionID));
        if (null == id) {
            id = 0;
        }
        String answer1 = cursor.getString(cursor.getColumnIndex(Answer1));
        String answer2 = cursor.getString(cursor.getColumnIndex(Answer2));
        String answer3 = cursor.getString(cursor.getColumnIndex(Answer3));
        String answer4 = cursor.getString(cursor.getColumnIndex(Answer4));

//        int typeA = cursor.getInt(cursor.getColumnIndex(TypeA));
//        int typeB = cursor.getInt(cursor.getColumnIndex(TypeB));
//        int typeC = cursor.getInt(cursor.getColumnIndex(TypeC));
//        int typeD = cursor.getInt(cursor.getColumnIndex(TypeD));
//        int typeQuestion = cursor.getInt(cursor.getColumnIndex(TypeQuestion));

        int answerRight = cursor.getInt(cursor.getColumnIndex(AnswerTrue));
        List<Answer> answerList = new ArrayList<>();
        if (answerRight == 1) {
            answerList.add(new Answer(answer1, true));
            answerList.add(new Answer(answer2, false));
            answerList.add(new Answer(answer3, false));
            answerList.add(new Answer(answer4, false));
        } else if (answerRight == 2) {
            answerList.add(new Answer(answer1, false));
            answerList.add(new Answer(answer2, true));
            answerList.add(new Answer(answer3, false));
            answerList.add(new Answer(answer4, false));
        } else if (answerRight == 3) {
            answerList.add(new Answer(answer1, false));
            answerList.add(new Answer(answer2, false));
            answerList.add(new Answer(answer3, true));
            answerList.add(new Answer(answer4, false));
        } else if (answerRight == 4) {
            answerList.add(new Answer(answer1, false));
            answerList.add(new Answer(answer2, false));
            answerList.add(new Answer(answer3, false));
            answerList.add(new Answer(answer4, true));
        }
        return new Question(
                id,
                cursor.getString(cursor.getColumnIndex(Question)),
                cursor.getString(cursor.getColumnIndex(Image)),
                answerList,
                cursor.getInt(cursor.getColumnIndex(CategoryID))
        );
    }
}
