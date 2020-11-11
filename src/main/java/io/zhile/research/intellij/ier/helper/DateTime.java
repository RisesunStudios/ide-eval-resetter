package io.zhile.research.intellij.ier.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static final DateFormat DF_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getStringFromTimestamp(long timestamp) {
        Date date = new Date(timestamp);

        return DF_DATETIME.format(date);
    }
}
