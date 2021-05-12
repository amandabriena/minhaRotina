package com.example.meuprojeto.controller;

import com.example.meuprojeto.datasource.DataSource;
import com.example.meuprojeto.model.Atividade;

import java.util.ArrayList;
import java.util.List;

public class AtividadeController extends DataSource {
    private Atividade atividade;

    public AtividadeController(){
        this.atividade = new Atividade();
    }

    //método para incluir atividade
    public void incluirAtividade(Atividade atv){
        //TODO - incluindo atividade na base de dados
        this.atividade = atividade;
        //adicionarAtividade(atividade);
    }
    public boolean deletarAtividade(int id_atividade){
        //TODO: Deletar Registros
        return true;
    }

    public boolean deletarAtividade(Atividade atv){
        //TODO: Deletar Registros
        return true;
    }

    public List<String> listarAtividades(){
        //TODO: Listar atividades
        List<String> lista = new ArrayList<>();

        lista.add("Acordar");
        lista.add("Tomar Banho");
        lista.add("Dormir");
        lista.add("Estudar");
        lista.add("Brincar");
        lista.add("Teste");

        return lista;
    }

    public List<String> listarDados(){
        return listarAtividades();
    }

}
