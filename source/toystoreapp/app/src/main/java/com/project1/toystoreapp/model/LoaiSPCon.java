package com.project1.toystoreapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoaiSPCon implements Serializable {
    @SerializedName("_id")
    private String id;

    public LoaiSPCon() {
    }

    private String parentID;
    private String tenloai;
    private int isActivate;

    public LoaiSPCon(String parentID, String tenloai, int isActivate) {
        this.parentID = parentID;
        this.tenloai = tenloai;
        this.isActivate = isActivate;
    }
    public boolean isActivated(){
        return isActivate==1;
    }
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
    public static LoaiSPCon clone(LoaiSPCon original) {
        if (original == null) return null;
        LoaiSPCon cloned = new LoaiSPCon(original.parentID, original.tenloai, original.isActivate);
        cloned.setId(original.getId());
        return cloned;
    }
}
