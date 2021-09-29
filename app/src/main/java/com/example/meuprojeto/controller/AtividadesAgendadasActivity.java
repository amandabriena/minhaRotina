package com.example.meuprojeto.controller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.type.Color;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;

public class AtividadesAgendadasActivity extends AppCompatActivity {
    //MaterialCalendarView calendario;

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()) ;
    private TextView mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades_agendadas);
        mes = (TextView) findViewById(R.id.mes);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        //Setando informação do mês atual
        Date d = new Date();
        final Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(DAY_OF_MONTH, 4);
        mes.setText(dateFormatMonth.format(d));

        //Setando evento:

        long time = cal.getTimeInMillis();
        Log.e("Alarme", "Time agenda: "+cal.getTime());
        Event ev1 = new Event(R.color.colorPrimary, time, "DIA HOJE");
        compactCalendar.addEvent(ev1);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Log.e("Alarme", "Date: "+cal.getTime())
                if(dateClicked.compareTo(cal.getTime())== 1){
                    Toast.makeText(AtividadesAgendadasActivity.this, "DIA D", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mes.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


    }
}
