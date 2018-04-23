package by.i4t.log;

import by.i4t.helper.GisunExportErrors;


public class Test {

    public static void main(String[] arg) {
//		Locale locale = new Locale("ru", "RU");
//		Date date = Calendar.getInstance(locale).getTime();
//		System.out.println(date);
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//	    cal.set(Calendar.HOUR_OF_DAY, 23);
//	    cal.set(Calendar.MINUTE, 59);
//	    cal.set(Calendar.SECOND, 59);
//	    System.out.println(cal.getTime());

        System.out.println(GisunExportErrors.AUTH_ERROR.toString());
        System.out.println(GisunExportErrors.AUTH_ERROR.name());
        System.out.println(GisunExportErrors.valueByCode("08").toString());
    }
}
