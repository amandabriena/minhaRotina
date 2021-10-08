package com.example.meuprojeto.model;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

public class AtividadeAgendada extends Atividade {
    //alterar datas para tipo Date
    private Date data;
    private String dataPrevia;
    private String urlVideo;

    public AtividadeAgendada() {
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof AtividadeAgendada){
            if(this.getId().
                    equals(((AtividadeAgendada) obj).getId())){
                return true;
            }
        }
        return false;
    }
}
