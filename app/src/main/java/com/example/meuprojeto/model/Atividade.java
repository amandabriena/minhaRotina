package com.example.meuprojeto.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.UUID;

public class Atividade {
    private String id = UUID.randomUUID().toString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
