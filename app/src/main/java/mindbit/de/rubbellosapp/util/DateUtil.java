package mindbit.de.rubbellosapp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private final static String DEFAULT_DATE_PATTERN = "dd.MM.yyyy HH:mm";

    public static String getCurrentDateAsString(Locale locale){
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN, locale);
        return simpleDateFormat.format(date);
    }
}
