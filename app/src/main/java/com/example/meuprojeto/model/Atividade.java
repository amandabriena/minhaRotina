package com.example.meuprojeto.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Atividade implements Parcelable {
    private String id = (new Random().nextInt(999999))+"";
    private String idUsuario;
    private String nomeAtividade;
    private String imagemURL;
    private String horario;
    private String musica;
    private ArrayList<String> dias_semana;
    private String status;
    //total realizada define a quantidade de vezes total em que a atividade ocorreu, sendo concluida ou não:
    private int totalRealizada;
    private int qtVezesConcluida;
    private int somatorioFeedBack;
    //Define se será ou não perguntado o feedback da atividade ao finalizá-la:
    private boolean feedbackAtivo;

    /*Status da atividade poderá ser:
    0- em aberto
    1- concluída
    2- não realizada
    */


    public Atividade(){
    }

    public Atividade(String nomeAtividade, String imagemURL, String horario, String musica) {
        //this.id = UUID.randomUUID().toString();
        this.nomeAtividade = nomeAtividade;
        this.imagemURL = imagemURL;
        this.horario = horario;
        this.musica = musica;
    }

    public Atividade(String nomeAtividade, String horario, String musica) {
        this.id = UUID.randomUUID().toString();
        this.nomeAtividade = nomeAtividade;
        this.horario = horario;
        this.musica = musica;
    }

    public Atividade(String id) {
        this.id = id;
    }

    protected Atividade(Parcel in) {
        id = in.readString();
        idUsuario = in.readString();
        nomeAtividade = in.readString();
        imagemURL = in.readString();
        horario = in.readString();
        musica = in.readString();
        dias_semana = in.createStringArrayList();
        status = in.readString();
    }

    public boolean isFeedbackAtivo() {
        return feedbackAtivo;
    }

    public void setFeedbackAtivo(boolean feedbackAtivo) {
        this.feedbackAtivo = feedbackAtivo;
    }

    public static final Creator<Atividade> CREATOR = new Creator<Atividade>() {
        @Override
        public Atividade createFromParcel(Parcel in) {
            return new Atividade(in);
        }

        @Override
        public Atividade[] newArray(int size) {
            return new Atividade[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ArrayList<String> getDias_semana() {
        return dias_semana;
    }

    public void setDias_semana(ArrayList<String> dias_semana) {
        this.dias_semana = dias_semana;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMusica() {
        return musica;
    }

    public void setMusica(String musica) {
        this.musica = musica;
    }

    public String getImagemURL() {
        return imagemURL;
    }

    public void setImagemURL(String imagemURL) {
        this.imagemURL = imagemURL;
    }



    public int getSomatorioFeedBack() {
        return somatorioFeedBack;
    }

    public void setSomatorioFeedBack(int somatorioFeedBack) {
        this.somatorioFeedBack = somatorioFeedBack;
    }

    public int getTotalRealizada() {
        return totalRealizada;
    }

    public void setTotalRealizada(int totalRealizada) {
        this.totalRealizada = totalRealizada;
    }

    public int getQtVezesConcluida() {
        return qtVezesConcluida;
    }

    public void setQtVezesConcluida(int qtVezesConcluida) {
        this.qtVezesConcluida = qtVezesConcluida;
    }

    public static Creator<Atividade> getCREATOR() {
        return CREATOR;
    }

    @NonNull
    @Override
    public String toString() {
        return nomeAtividade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idUsuario);
        dest.writeString(nomeAtividade);
        dest.writeString(imagemURL);
        dest.writeString(horario);
        dest.writeString(musica);
        dest.writeStringList(dias_semana);
        dest.writeString(status);
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Passo){
            if(this.getId().
                    equals(((Atividade) obj).getId())){
                return true;
            }
        }
        return false;
    }
}
