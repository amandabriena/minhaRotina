package com.example.meuprojeto.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.AtividadeAgendada;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.Color;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.meuprojeto.util.Utilitarios.verificarDiaSemana;
import static java.util.Calendar.DAY_OF_MONTH;

public class AtividadesAgendadasActivity extends AppCompatActivity {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()) ;
    private TextView mes;
    private List<AtividadeAgendada> listaAtividades = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades_agendadas);
        mes = (TextView) findViewById(R.id.mes);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        new CarregarListaAsynctask().execute();

        //Setando informação do mês atual
        Date d = new Date();
        final Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        mes.setText(dateFormatMonth.format(d));


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                List<AtividadeAgendada> atividadesDia = buscarAtividadesData(events);
                Log.e("Alarme", "Day was clicked: " + dateClicked + " with events " + events);
                if(atividadesDia.size() > 0){
                    Intent intent = new Intent(AtividadesAgendadasActivity.this, PopUpAtividadesAgendadas.class);
                    intent.putParcelableArrayListExtra("atividades", (ArrayList<? extends Parcelable>) atividadesDia);
                    startActivity(intent);
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mes.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


    }
    public class CarregarListaAsynctask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // carregar do banco
            Log.e("Alarme", "carregando atividades..");
            final String dia = verificarDiaSemana();
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(FirebaseAuth.getInstance().getUid()).collection("atividadesAgendadas")
                    .orderBy("data", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                Log.e("Erro", error.getMessage());
                                return;
                            }
                            List<DocumentSnapshot> docs = value.getDocuments();
                            for(DocumentSnapshot doc : docs){
                                AtividadeAgendada atv = doc.toObject(AtividadeAgendada.class);
                                listaAtividades.add(atv);
                                incluirEvento(atv);
                                Log.e("Alarme", "Atividade Agendada: "+atv.getNomeAtividade());

                            }
                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Void resultado) {
        }
    }
    //Incluindo eventos:
    public void incluirEvento(AtividadeAgendada atv){
        Event evento = new Event(R.color.colorPrimary, atv.getData().getTime(), atv.getNomeAtividade());
        compactCalendar.addEvent(evento);
        Log.e("Alarme", "Evento add: "+atv.getNomeAtividade());
        Log.e("Alarme", "Evento add time: "+atv.getData().getTime());
        Log.e("Alarme", "Evento time: "+evento.getTimeInMillis());
    }

    public List<AtividadeAgendada> buscarAtividadesData(List<Event> events){
        Log.e("Alarme", "busca por atividades");
        List<AtividadeAgendada> listaAtividadesDia = new ArrayList<>();
        for (Event e: events) {
            Log.e("Alarme", "busca evento"+e.getTimeInMillis());
            for (AtividadeAgendada atividadeAgendada: listaAtividades) {
                Log.e("Alarme", "busca data"+atividadeAgendada.getData().getTime());
                if((atividadeAgendada.getData().getTime()+"").equals(e.getTimeInMillis()+"")){
                    Log.e("Alarme", "Atividade adicionada: "+atividadeAgendada.getNomeAtividade());
                    listaAtividadesDia.add(atividadeAgendada);
                }
            }
        }

        return listaAtividadesDia;
    }
}
