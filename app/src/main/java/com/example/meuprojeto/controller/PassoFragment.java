package com.example.meuprojeto.controller;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meuprojeto.R;
import com.example.meuprojeto.model.Passo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PassoFragment extends Fragment {
    Passo objPasso;

    //Declarando variáveis
    TextView ordemPasso;
    EditText descricao, som;
    Button btFinalizarPassos, btAddPassos;
    ImageButton btUploadImg, btStart, btPlay, btTrash;
    ImageView imgPasso;
    int numPasso;
    private Uri filePath;
    private byte[] dataIMG;
    String idAtividade, passo;
    MediaRecorder mediaRecorder;
    private static int MICROPHONE_PERMISSION_CODE = 200;
    //Conexão com o db
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
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
            passo = bundleRetorno.getString("passoAtual");
            idAtividade = bundleRetorno.getString("idAtividade");
            listaPassos = bundleRetorno.getParcelableArrayList("lista");
        }
        numPasso = Integer.parseInt(passo);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passo, container, false);
        descricao = v.findViewById(R.id.descricao);
        som = v.findViewById(R.id.som);
        ordemPasso = v.findViewById(R.id.ordemPasso);
        btFinalizarPassos = v.findViewById(R.id.btFinalizar);
        btAddPassos = v.findViewById(R.id.btAddPassos);
        btUploadImg = v.findViewById(R.id.btUploadImgPasso);
        imgPasso = v.findViewById(R.id.imgPasso);
        btStart = v.findViewById(R.id.btStart);
        btPlay = v.findViewById(R.id.btPlay);
        btTrash = v.findViewById(R.id.btTrash);

        //Inicializando o objeto do passo atual para cadastro
        objPasso = new Passo();
        //objPasso.setNumOrdem(passo);

        //Setando a informação do passo atual no fragmento:
        ordemPasso.setText("Passo "+passo+":");


        /*
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
        });*/
        return v;
    }
}
