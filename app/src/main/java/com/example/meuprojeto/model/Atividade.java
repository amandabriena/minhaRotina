package com.example.meuprojeto.model;

import androidx.annotation.NonNull;

import java.util.UUID;

public class Atividade {
    private String id = UUID.randomUUID().toString();
    private String nome_atividade;
    private String imagemURL;
    private String horario;
    private String musica;

    public Atividade(){

    }

    public Atividade(String nome_atividade, String imagemURL, String horario, String musica) {
        //this.id = UUID.randomUUID().toString();
        this.nome_atividade = nome_atividade;
        this.imagemURL = imagemURL;
        this.horario = horario;
        this.musica = musica;
    }

    public Atividade(String nome_atividade, String horario, String musica) {
        this.id = UUID.randomUUID().toString();
        this.nome_atividade = nome_atividade;
        this.horario = horario;
        this.musica = musica;
    }

    public Atividade(String nome_atividade){
        this.nome_atividade = nome_atividade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome_atividade() {
        return nome_atividade;
    }

    public void setNome_atividade(String nome_atividade) {
        this.nome_atividade = nome_atividade;
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
        return nome_atividade;
    }
}
