package com.example.meuprojeto.controller;

import com.example.meuprojeto.datasource.DataSource;
import com.example.meuprojeto.model.Atividade;

import java.util.List;

public class AtividadeController extends DataSource {
    private Atividade atividade;

    public AtividadeController(){
        this.atividade = new Atividade();
    }

    //método para incluir atividade
    public void incluirAtividade(Atividade atividade){
        //TODO - incluindo atividade na base de dados
        this.atividade = atividade;
        persisirDados(atividade);
    }

    public List<String> listarDados(){
        return listarAtividades();
    }

}
