package com.example.meuprojeto.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.UUID;

public class Atividade implements Parcelable {
    private String id = UUID.randomUUID().toString();
    private String idUsuario;
    private String nomeAtividade;
    private String imagemURL;
    private String horario;
    private String musica;
    private ArrayList<String> dias_semana;
    private int status;
    /*Status da atividade poderá ser:
    1- em aberto
    2- concluída
    3- não realizada
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

    public Atividade(String nomeAtividade){
        this.nomeAtividade = nomeAtividade;
    }

    protected Atividade(Parcel in) {
        id = in.readString();
        idUsuario = in.readString();
        nomeAtividade = in.readString();
        imagemURL = in.readString();
        horario = in.readString();
        musica = in.readString();
        dias_semana = in.createStringArrayList();
        status = in.readInt();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
        dest.writeInt(status);
    }
}
