package com.app.maththpt.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by FRAMGIA\nguyen.duc.manh on 15/02/2017.
 */

public class FacebookUtils {
    public static String getFacebookID() {
        if (getCurrentAccessToken() == null) {
            return "";
        }
        return getCurrentAccessToken().getUserId();
    }

    public static String getFacebookToken() {
        if (getCurrentAccessToken() == null) {
            return "";
        }
        return getCurrentAccessToken().getToken();
    }

    public static boolean isExpires() {
        if (getCurrentAccessToken() == null) {
            return true;
        }
        Date sExpires = getCurrentAccessToken().getExpires();
        long lExpires = sExpires.getTime();
        long currentTime = System.currentTimeMillis();
        return lExpires <= currentTime;
    }

    public static String getAvatarFBFromAddress(String address) {
        URL url;
        String newLocation = null;
        try {
            url = new URL(address);
            HttpURLConnection.setFollowRedirects(false); //Do _not_ follow redirects!
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            newLocation = connection.getHeaderField("Location");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return newLocation;
    }

    public static String getAvatarFBFromId() {
        if (getFacebookID() == null || getFacebookID().isEmpty()) {
            return "";
        }
        return "https://graph.facebook.com/" + getFacebookID() + "/picture?type=large";
    }
}
