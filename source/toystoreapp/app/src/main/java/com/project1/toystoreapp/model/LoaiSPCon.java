package com.project1.toystoreapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoaiSPCon {
    @SerializedName("_id")
    private String id;

    private String parentID;
    private String tenloai;
    private int isActivate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public int getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(int isActivate) {
        this.isActivate = isActivate;
    }
}
