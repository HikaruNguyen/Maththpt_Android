package com.app.maththpt.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * Created by manhi on 3/1/2017.
 */

public class Utils {
    public static String encodeToBase64(
            Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static Bitmap getScreenShot(View v) {
        Bitmap b;
        b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);

        return b;
    }

    public static String replaceMath(String string) {
        if (string != null) {
            string = string
                    .replace("xmlns=\"http://www.w3.org/1998/Math/MathML\"", "")
                    .replace("xmlns='http://www.w3.org/1998/Math/MathML'", "");
            if (string.startsWith("<p>")) {
                string = string.substring(3, string.length());
            }
            if (string.endsWith("</p>")) {
                string = string.substring(0, string.length() - 4);
            }
        } else {
            return "";
        }
        return string;
    }
}
