package com.example.meuprojeto.datasource;

import com.example.meuprojeto.model.Atividade;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public boolean persisirDados(Atividade obj){
        if(true){
            //TODO: Adicionar Registros
        }else{
            //TODO: Alterar Registros
        }
        return true;
    }

    public boolean deletarAtividade(int id_atividade){
        //TODO: Deletar Registros
        return true;
    }

    public boolean deletarAtividade(Atividade obj){
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

        return lista;
    }
}
