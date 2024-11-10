package com.project1.toystoreapp.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.project1.toystoreapp.Classes.ThuongHieu;

import java.util.List;

public class SanPham {
    @SerializedName("_id")
    private String id;
    private String tensanpham;
    private long gia;
    private String mota;
    private List<LoaiSPCon> loaiSPCon;
    private ThuongHieu thuonghieu;
    private Integer isActivate;
    private Integer isInMainScreen;
    private String urrlanh;

    public SanPham(String tensanpham, long gia, String mota, ThuongHieu thuonghieu, @Nullable Integer isActivate,@Nullable Integer isInMainScreen, String urrlanh) {
        this.tensanpham = tensanpham;
        this.gia = gia;
        this.mota = mota;
        this.thuonghieu = thuonghieu;
        this.isActivate = isActivate;
        this.isInMainScreen = isInMainScreen;
        this.urrlanh = urrlanh;
    }

    public String getUrrlanh() {
        return urrlanh;
    }

    public void setUrrlanh(String urrlanh) {
        this.urrlanh = urrlanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public long getGia() {
        return gia;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public List<LoaiSPCon> getLoaiSPCon() {
        return loaiSPCon;
    }

    public void setLoaiSPCon(List<LoaiSPCon> loaiSPCon) {
        this.loaiSPCon = loaiSPCon;
    }

    public ThuongHieu getThuonghieu() {
        return thuonghieu;
    }

    public void setThuonghieu(ThuongHieu thuonghieu) {
        this.thuonghieu = thuonghieu;
    }

    public Integer getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(int isActivate) {
        this.isActivate = isActivate;
    }

    public Integer getIsInMainScreen() {
        return isInMainScreen;
    }

    public void setIsInMainScreen(int isInMainScreen) {
        this.isInMainScreen = isInMainScreen;
    }

    @NonNull
    @Override
    public String toString() {
        return getTensanpham();
    }
}
