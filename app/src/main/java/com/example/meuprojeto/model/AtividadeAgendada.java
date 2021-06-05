package com.example.meuprojeto.model;

import java.util.Date;
import java.util.UUID;

public class AtividadeAgendada extends Atividade {
    //alterar datas para tipo Date
    private String data;
    private String dataPrevia;
    private String urlVideo;

    public AtividadeAgendada() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataPrevia() {
        return dataPrevia;
    }

    public void setDataPrevia(String dataPrevia) {
        this.dataPrevia = dataPrevia;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
