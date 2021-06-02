package com.example.meuprojeto.datasource;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.meuprojeto.datasource.DataSource;
import com.example.meuprojeto.model.Atividade;
import com.example.meuprojeto.model.Passo;
import com.example.meuprojeto.model.Usuario;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AtividadeController extends DataSource {
    private Atividade atividade;

    public AtividadeController(){
        this.atividade = new Atividade();
    }

    //método para incluir atividade
    public void incluirAtividade(Atividade atv){
        //TODO - incluindo atividade na base de dados
        this.atividade = atividade;
        //adicionarAtividade(atividade);
    }
    public boolean deletarAtividade(int id_atividade){
        //TODO: Deletar Registros
        return true;
    }

    public boolean deletarAtividade(Atividade atv){
        //TODO: Deletar Registros
        return true;
    }

    public List<Passo> listarPassosAtividade(String idAtividade){
        final List<Passo> passosAtv = new ArrayList<>();
        //TODO: Listar atividades
        FirebaseFirestore.getInstance().collection("atividades").document(idAtividade)
                .collection("/passos").orderBy("ordem", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Erro", error.getMessage());
                            return;
                        }
                        List<DocumentSnapshot> docs = value.getDocuments();
                        for(DocumentSnapshot doc : docs){
                            Passo passo = doc.toObject(Passo.class);
                            passosAtv.add(passo);
                        }
                    }
                });

        return passosAtv;
    }

    public List<Atividade> listarAtividadesUsuario(String idUsuario){
        final List<Atividade> listaAtividades = new ArrayList<>();
        //TODO: Listar atividades
        FirebaseFirestore.getInstance().collection("usuarios").document(idUsuario)
                .collection("/atividades").orderBy("horario", Query.Direction.ASCENDING)
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
                            listaAtividades.add(atv);
                        }
                    }
                });

        return listaAtividades;
    }



}
