package io.zhile.research.intellij.ier.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {
    public static final DateFormat DF_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat DF_PLUGIN_DATE = new SimpleDateFormat("yyyyMMdd", Locale.US);

    public static String getStringFromTimestamp(long timestamp) {
        Date date = new Date(timestamp);

        return DF_DATETIME.format(date);
    }

    public static String getPluginReleaseDateStr(Date releaseDate) {
        return DF_PLUGIN_DATE.format(releaseDate);
    }
}
