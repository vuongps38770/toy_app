package com.project1.toystoreapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoaiSP {
    @SerializedName("_id")
    private String id;
    private String tenloai;
    private int isActivate;
    public int sl;
    @SerializedName("listLoaiSPConID")
    private List<LoaiSPCon> listLSPCON;
    public LoaiSP(String id, String tenloai, int isActivate) {
        this.id = id;
        this.tenloai = tenloai;
        this.isActivate = isActivate;
    }
    public List<LoaiSPCon> getListLSPCON() {
        return listLSPCON;
    }
    public void setListLSPCON(List<LoaiSPCon> listLSPCON) {
        this.listLSPCON = listLSPCON;
    }
    public LoaiSP(LoaiSP clone) {
        this.id = clone.id;
        this.tenloai = clone.tenloai;
        this.isActivate = clone.isActivate;
    }
    public LoaiSP(String tenloai, int isActivate) {
        this.tenloai = tenloai;
        this.isActivate = isActivate;
    }
    public boolean isActivate(){
        return isActivate==1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
