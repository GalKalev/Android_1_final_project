package com.example.finalprojectandroid1;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GlobalMembers {

    static String TAG = "GlobalMembers";
    public static String[] citiesList = {"בחר עיר","אופקים", "אור יהודה","אור עקיבא", "אילת", "אלעד",
            "אריאל", "אשדוד", "אשקלון", "באר יעקב", "באר שבע", "בית שאן",
            "בית שמש", "ביתר עילית", "בני ברק", "בת ים", "גבעת שמואל", "גבעתיים",
            "גני תקווה", "דימונה", "הוד השרון", "הרצליה","חדרה","חולון","חיפה",
            "טבריה" , "טירת כרמל", "יבנה", "יהוד-מונוסון", "יקנעם עילית", "ירושלים",
            "כפר יונה","כפר סבא","כרמיאל","לוד","מגדל העמק","מודיעין עילית","מודיעין-מכבים-רעות",
            "מעלה אדומים","מעלות-תרשיחא","נהריה","נוף הגליל","נס ציונה",
            "נשר (עיר)","נתיבות","נתניה","עכו","עפולה","ערד","פתח תקווה",
            "צפת","קריית אונו","קריית אתא","קריית ביאליק","קריית גת",
            "קריית ים","קריית מוצקין","קריית מלאכי","קריית שמונה",
            "ראש העין","ראשון לציון","רחובות","רמלה","רמת גן",
            "רמת השרון","רעננה","שדרות","תל אביב-יפו"};

    public static int todayDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String monthFix;
        if(month < 10){
            monthFix = "0" + month;

        }else{
            monthFix = String.valueOf(month);
        }
        String dayFix;

        if(day < 10){
            dayFix = "0" + day;
        }else{
            dayFix = String.valueOf(day);
        }
        return  Integer.parseInt(year + monthFix + dayFix);
    }

    public static int timeRightNow(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String hourFix;
        if(hour < 10){
            hourFix = "0" + hour;

        }else{
            hourFix = String.valueOf(hour);
        }

        String minuteFix;
        if(minute < 10){
            minuteFix = "0" + minute;

        }else{
            minuteFix = String.valueOf(minute);
        }

        return Integer.parseInt(hourFix + minuteFix);

    }

    public static String convertDateFromShowToCompare(String dateToConvert){
        String dateCompare = null;
        try{
            SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdfInput.parse(dateToConvert);

            SimpleDateFormat sdfCompare = new SimpleDateFormat("yyyyMMdd");
            dateCompare = sdfCompare.format(date);


        }catch (Exception e){
            Log.d(TAG,"error converting date: " + e.getMessage());

        }
        return dateCompare;
    }
    public static String convertDateFromCompareToShow(String dateToConvert){
        String dateCompare = null;
        try{
            SimpleDateFormat sdfInput = new SimpleDateFormat( "yyyyMMdd");
            Date date = sdfInput.parse(dateToConvert);

            SimpleDateFormat sdfShow = new SimpleDateFormat("dd/MM/yyyy");
            dateCompare = sdfShow.format(date);


        }catch (Exception e){
            Log.d(TAG,"error converting date: " + e.getMessage());

        }
        return dateCompare;
    }

    public static String formattingTimeToString(int time){
        String timeString;
        if(time < 1000){
            timeString = "0" + time;
        }else{
            timeString = String.valueOf(time);
        }
        timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);

        return timeString;
    }


}
