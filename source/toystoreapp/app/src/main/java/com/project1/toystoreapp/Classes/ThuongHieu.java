package com.project1.toystoreapp.Classes;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ThuongHieu {
    @SerializedName("_id")
    private String id;
    private String tenthuonghieu;
    private String urlthumbnail;

    public ThuongHieu(String tenthuonghieu, String urlthumbnail) {
        this.tenthuonghieu = tenthuonghieu;
        this.urlthumbnail = urlthumbnail;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenthuonghieu() {
        return tenthuonghieu;
    }

    public void setTenthuonghieu(String tenthuonghieu) {
        this.tenthuonghieu = tenthuonghieu;
    }

    public String getUrlthumbnail() {
        return urlthumbnail;
    }

    public void setUrlthumbnail(String urlthumbnail) {
        this.urlthumbnail = urlthumbnail;
    }

    @NonNull
    @Override
    public String toString() {
        return getTenthuonghieu();
    }
}
