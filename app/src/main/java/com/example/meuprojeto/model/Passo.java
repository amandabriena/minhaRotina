package com.example.meuprojeto.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class Passo implements Parcelable {
    private String id = UUID.randomUUID().toString();
    private int numOrdem;
    private String descricaoPasso;
    private String imagemURL;
    private String audio;

    public Passo() {
    }

    public Passo(int numOrdem, String descricaoPasso, String imagemURL, String audio) {
        this.numOrdem = numOrdem;
        this.descricaoPasso = descricaoPasso;
        this.imagemURL = imagemURL;
        this.audio = audio;
    }

    protected Passo(Parcel in) {
        numOrdem = in.readInt();
        id = in.readString();
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

    public int getNumOrdem() {
        return numOrdem;
    }

    public void setNumOrdem(int numOrdem) {
        this.numOrdem = numOrdem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numOrdem);
        dest.writeString(id);
        dest.writeString(descricaoPasso);
        dest.writeString(imagemURL);
        dest.writeString(audio);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Passo){
            if(this.getId().
                    equals(((Passo) obj).getId())){
                return true;
            }
        }
        return false;
    }
}
