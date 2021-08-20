package com.example.meuprojeto.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.meuprojeto.model.Atividade;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Alarme extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Alarme para atualizar status das atividades
        FirebaseFirestore.getInstance().collection("usuarios")
                .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                .orderBy("horario", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Erro", error.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = value.getDocuments();
                        for(DocumentSnapshot doc : docs){
                            Atividade atv = doc.toObject(Atividade.class);
                            Log.e("Alarme", "Update status:"+atv.getNomeAtividade());
                            FirebaseFirestore.getInstance().collection("usuarios")
                                    .document(FirebaseAuth.getInstance().getUid()).collection("atividades")
                                    .document(atv.getId()).update("status", "0");
                        }

                    }
                });
        Log.e("Alarme", "Atualizado Horário");
    }
}
