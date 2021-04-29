package com.example.meuprojeto.model;

public class Atividade {
    private Integer id;
    private String nome_atividade;
    private String horario;
    private String musica;

    public Atividade(){

    }
    public Atividade(String nome_atividade){
        this.nome_atividade = nome_atividade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
