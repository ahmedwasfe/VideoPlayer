package com.oceanforit.videoplayer.common;

public class Common {

    public static final String KEY_POSITION = "position";
    public static final String KEY_FOLDER_NAME = "folder_name";

    public static String formattedTime(int currentPosition) {

        String totalOut = "";
        String totalNew = "";
        String secounds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + secounds;
        totalNew = minutes + ":" + "0" + secounds;
        if (secounds.length() == 1)
            return totalNew;
        else
            return totalOut;
    }
}
