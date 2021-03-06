package com.example.meuprojeto.model;

import java.util.UUID;

public class Usuario {
    private String id;
    private String nome;
    private String imagemURL;
    private String data;
    private String email;
    private String senha;


    public Usuario(String id, String nome, String imagemURL, String data, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.imagemURL = imagemURL;
        this.data = data;
        this.email = email;
        this.senha = senha;
    }

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagemURL() {
        return imagemURL;
    }

    public void setImagemURL(String imagemURL) {
        this.imagemURL = imagemURL;
    }
}
