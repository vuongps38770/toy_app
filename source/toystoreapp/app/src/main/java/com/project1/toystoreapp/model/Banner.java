package com.project1.toystoreapp.model;

public class Banner {
    private String url1;
    private String url2;
    private String url3;

    public Banner(String url1, String url2, String url3) {
        this.url1 = url1;
        this.url2 = url2;
        this.url3 = url3;
    }

    public Banner() {
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }
}
