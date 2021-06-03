package com.example.meuprojeto.model;

import java.util.ArrayList;

public class Passo {
    private String numOrdem;
    private String descricaoPasso;
    private String imagemURL;
    private String audio;

    public Passo() {
    }

    public Passo(String numOrdem, String descricaoPasso, String imagemURL, String audio) {
        this.numOrdem = numOrdem;
        this.descricaoPasso = descricaoPasso;
        this.imagemURL = imagemURL;
        this.audio = audio;
    }

    public String getDescricaoPasso() {
        return descricaoPasso;
    }

    public void setDescricaoPasso(String descricaoPasso) {
        this.descricaoPasso = descricaoPasso;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImagemURL() {
        return imagemURL;
    }

    public void setImagemURL(String imagemURL) {
        this.imagemURL = imagemURL;
    }

    public String getNumOrdem() {
        return numOrdem;
    }

    public void setNumOrdem(String numOrdem) {
        this.numOrdem = numOrdem;
    }
}
