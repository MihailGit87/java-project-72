package hexlet.code.util;

import java.sql.Timestamp;
import java.util.Date;

public final class Time {
    public static Timestamp getCurrentTime() {
        var date = new Date();
        return new Timestamp(date.getTime());
    }
}
