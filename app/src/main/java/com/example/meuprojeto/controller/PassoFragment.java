package com.example.meuprojeto.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PassoFragment extends Fragment {
    String numP;
    int numPasso;
    TextView passoAtualF, descricaoPassoF;
    Button  btProximo, btAnterior;
    ImageView imgPasso;
    private List<Passo> listaPassos = new ArrayList<>();
    public PassoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Pegando retorno do fragmento anterior caso houver:
        Bundle bundleRetorno = this.getArguments();
        if(bundleRetorno != null){
            numP = bundleRetorno.getString("ordemPasso");
            listaPassos = bundleRetorno.getParcelableArrayList("lista");
        }
        numPasso = Integer.parseInt(numP);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passo, container, false);
        passoAtualF = v.findViewById(R.id.numPassoAtualFrag);
        descricaoPassoF = v.findViewById(R.id.descricaoPassoFrag);
        btAnterior = v.findViewById(R.id.btAnteriorFrag);
        btProximo = v.findViewById(R.id.btProximoFrag);
        imgPasso = v.findViewById(R.id.imgPassoAtualFrag);

        //Setando as informações no fragmento:
        descricaoPassoF.setText(listaPassos.get(numPasso-1).getDescricaoPasso());
        passoAtualF.setText("Passo "+numP);
        Picasso.get().load(listaPassos.get(numPasso-1).getImagemURL()).into(imgPasso);

        btProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numPasso++;
                String num = String.valueOf(numPasso);

                Bundle bundle = new Bundle();
                bundle.putString("ordemPasso", num);
                bundle.putParcelableArrayList("lista", (ArrayList<Passo>) listaPassos);
                PassoFragment p = new PassoFragment();
                p.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frag, p).commit();
            }
        });
        if(numPasso<=1){
            btAnterior.setAlpha(0);
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numPasso++;
                    String num = String.valueOf(numPasso);
                    Bundle bundle = new Bundle();
                    bundle.putString("ordemPasso", num);
                    bundle.putParcelableArrayList("lista", (ArrayList<Passo>) listaPassos);
                    PassoFragment p = new PassoFragment();
                    p.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frag, p).commit();
                }
            });
        }else if(listaPassos.size() == numPasso){
            btProximo.setText("CONCLUÍDO");
            btAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    numPasso--;
                    String num = String.valueOf(numPasso);
                    Bundle bundle = new Bundle();
                    bundle.putString("ordemPasso", num);
                    bundle.putParcelableArrayList("lista", (ArrayList<Passo>) listaPassos);
                    PassoFragment p = new PassoFragment();
                    p.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frag, p).commit();
                }
            });
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Direcionando a ação do botão para abrir a tela de atividades
                    /*
                    Intent dashboard = new Intent(PassosAtividadeActivity.this, MinhaRotinaActivity.class);
                    Toast.makeText(PassosAtividadeActivity.this,"Atividade Concluída", Toast.LENGTH_LONG).show();
                    dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboard);*/
                }
            });
        }else{
            btAnterior.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numPasso--;
                    String num = String.valueOf(numPasso);
                    Bundle bundle = new Bundle();
                    bundle.putString("ordemPasso", num);
                    bundle.putParcelableArrayList("lista", (ArrayList<Passo>) listaPassos);
                    PassoFragment p = new PassoFragment();
                    p.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frag, p).commit();
                }
            });
            btProximo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numPasso++;
                    String num = String.valueOf(numPasso);
                    Bundle bundle = new Bundle();
                    bundle.putString("ordemPasso", num);
                    bundle.putParcelableArrayList("lista", (ArrayList<Passo>) listaPassos);
                    PassoFragment p = new PassoFragment();
                    p.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frag, p).commit();
                }
            });
        }
        return v;
    }
}
