package com.app.maththpt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.app.maththpt.BuildConfig;
import com.app.maththpt.utils.CLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by manhi on 20/7/2016.
 */

public class AssetDatabaseOpenHelper {
    // tên file datebase trong thư mục assets

    private Context context;

    public AssetDatabaseOpenHelper(Context context) {
        this.context = context;
    }

    public SQLiteDatabase StoreDatabase(String DB_NAME) {
        ///data/data/{package của bạn}/databases
        String path = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases";
        File pathDb = new File(path);
        try {
            // kiểm tra nếu có thư mục databases thì sẽ tạo
            if (!pathDb.exists()) {
                pathDb.mkdir();
            }
            // kiểm tra file đó chưa tồn tại thì copy vào
            // điều kiện này tránh trường hợp mỗi lần mở ứng dụng sẽ copy vào.
            if (!new File(path + "/" + DB_NAME).exists()) {
                copy(path, DB_NAME);
            }
        } catch (IOException e) {
            CLog.d("IOException", e.getMessage());
        }
        return SQLiteDatabase.openDatabase(
                context.getDatabasePath(DB_NAME).getPath(),
                null,
                SQLiteDatabase.OPEN_READONLY);
    }

    /*
     * Thực hiện đọc file và ghi file
     */
    private void copy(String path, String DB_NAME) throws IOException {

        // mở file trong thư mục assets
        InputStream is = context.getAssets().open(DB_NAME);
        FileOutputStream fos = new FileOutputStream(path + "/" + DB_NAME);
        byte buffer[] = new byte[1024];
        int length;

        // đọc và ghi vào thư mục databases
        while ((length = is.read(buffer)) > 0) {
            fos.write(buffer, 0, length);
        }


        is.close();
        fos.flush();
        fos.close();
    }
}
