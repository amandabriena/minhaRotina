package com.example.meuprojeto.controller;

import androidx.annotation.NonNull;

import com.example.meuprojeto.model.Usuario;

public class UsuarioController {
    //Conectando o controle com a classe usuário
    private Usuario usuario;

    public UsuarioController(){
        this.usuario = new Usuario();
    }
    //método para incluir usuário
    public void incluirUsuario(Usuario usuario){
        //TODO - incluindo usuário na base de dados
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public String toString() {
        String dadosUsuario = "";
        dadosUsuario += "Nome: "+this.usuario.getNome()+"\n";
        dadosUsuario += "Email: "+this.usuario.getEmail()+"\n";
        dadosUsuario += "Data: "+this.usuario.getData()+"\n";
        dadosUsuario += "Senha: "+this.usuario.getSenha()+"\n";
        return dadosUsuario;
    }
}
