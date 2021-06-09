package com.example.meuprojeto.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Passo implements Parcelable {
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

    protected Passo(Parcel in) {
        numOrdem = in.readString();
        descricaoPasso = in.readString();
        imagemURL = in.readString();
        audio = in.readString();
    }

    public static final Creator<Passo> CREATOR = new Creator<Passo>() {
        @Override
        public Passo createFromParcel(Parcel in) {
            return new Passo(in);
        }

        @Override
        public Passo[] newArray(int size) {
            return new Passo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numOrdem);
        dest.writeString(descricaoPasso);
        dest.writeString(imagemURL);
        dest.writeString(audio);
    }
}
