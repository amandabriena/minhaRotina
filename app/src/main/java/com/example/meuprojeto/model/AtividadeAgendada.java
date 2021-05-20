package com.example.meuprojeto.model;

import java.util.Date;

public class AtividadeAgendada extends Atividade {
    private Date data_agendada;
    private Date data_previa;
    private String urlVideo;

    public Date getData_agendada() {
        return data_agendada;
    }

    public void setData_agendada(Date data_agendada) {
        this.data_agendada = data_agendada;
    }

    public Date getData_previa() {
        return data_previa;
    }

    public void setData_previa(Date data_previa) {
        this.data_previa = data_previa;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
