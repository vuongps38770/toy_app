package com.project1.toystoreapp.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SanPham {
    @SerializedName("_id")
    @NonNull
    private String id;
    private String tensanpham;
    private long gia;
    private String mota;
    private List<LoaiSPCon> loaiSPCon;
    
    private ThuongHieu thuonghieu;
    private Integer isActivate;
    private Integer isInMainScreen;
    private String urlanh;

    public SanPham(String tensanpham, long gia, String mota, ThuongHieu thuonghieu, @Nullable Integer isActivate,@Nullable Integer isInMainScreen, String urlanh) {
        this.tensanpham = tensanpham;
        this.gia = gia;
        this.mota = mota;
        this.thuonghieu = thuonghieu;
        this.isActivate = isActivate;
        this.isInMainScreen = isInMainScreen;
        this.urlanh = urlanh;
    }

    public String getUrlanh() {
        return urlanh;
    }

    public void setUrlanh(String urlanh) {
        this.urlanh = urlanh;
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
