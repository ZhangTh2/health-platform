package edu.zju.ccnt.user.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class


DateTimeUtil {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DAY_FROMAT = "yyyyMMdd";

    private static final String MONTH_FORMAT = "yyyyMM";


    //joda String ->date
    public static Date strToDate(String str,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    //data --> String
    public static String dateToStr(Date date,String formatStr){
       if(date == null){
           return StringUtils.EMPTY;
       }
       DateTime dateTime = new DateTime(date);
       return dateTime.toString(formatStr);
    }

    //joda String ->date
    public static Date strToDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    //joda String ->date
    public static Date strFormatToDate(String str,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    //data --> String
    public static String dateToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(DATE_FORMAT);
    }

    public static List<String> getTargetTimeInfo(String begin, String end){

        List<String> res = new ArrayList<>();
        if(begin.length()!=10 || end.length()!=10)
            return res;
        if(StringUtils.compare(begin, end) > 0){
            return res;
        }
        LocalDate localDate = new LocalDate(end);
        LocalDate beginDate = new LocalDate(begin);
        System.out.println(localDate.toString());
        System.out.println(beginDate.toString());
        //System.out.println(localDate.toString());
        while(true){
            System.out.println(1);
            if(beginDate.equals(localDate)){
                res.add(localDate.toString());
                break;
            }
            else{
                res.add(localDate.toString());
                localDate = localDate.minusDays(1);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(strFormatToDate("2017/01/15","yyyy/MM/dd"));
    }


}
