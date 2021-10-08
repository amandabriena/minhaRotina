package com.example.meuprojeto.model;

public class Avaliacao {
    private String id;
    private int avaliacao;
    private String id_atividade;

    public Avaliacao(String id, int avaliacao, String id_atividade) {
        this.id = id;
        this.avaliacao = avaliacao;
        this.id_atividade = id_atividade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getId_atividade() {
        return id_atividade;
    }

    public void setId_atividade(String id_atividade) {
        this.id_atividade = id_atividade;
    }
}
