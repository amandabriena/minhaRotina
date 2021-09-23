package com.example.meuprojeto.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class Utilitarios {

    public static String verificarDiaSemana(){
        Date d = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        String nome = "";
        int diaS = c.get(c.DAY_OF_WEEK);
        c.set(Calendar.HOUR_OF_DAY, d.getHours());
        c.set(Calendar.MINUTE, d.getMinutes());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Log.e("hora:", simpleDateFormat.format(c.getTime()));
        String dia = "";
        switch(diaS){
            case Calendar.SUNDAY:
                dia = "1";
                break;
            case Calendar.MONDAY:
                dia = "2";
                break;
            case Calendar.TUESDAY:
                dia = "3";
                break;
            case Calendar.WEDNESDAY:
                dia = "4";
                break;
            case Calendar.THURSDAY:
                dia = "5";
                break;
            case Calendar.FRIDAY:
                dia = "6";
                break;
            case Calendar.SATURDAY:
                dia = "7";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Calendar.DAY_OF_WEEK);
        }
        return dia;
    }

}
