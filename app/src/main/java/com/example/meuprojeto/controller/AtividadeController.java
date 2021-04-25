package com.example.meuprojeto.controller;

import com.example.meuprojeto.model.Atividade;

public class AtividadeController {
    private Atividade atividade;

    public AtividadeController(){
        this.atividade = new Atividade();
    }

    //método para incluir atividade
    public void incluirAtividade(Atividade atividade){
        //TODO - incluindo atividade na base de dados
        this.atividade = atividade;
    }
}
