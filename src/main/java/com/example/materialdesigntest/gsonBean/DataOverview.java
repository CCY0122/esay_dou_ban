package com.example.materialdesigntest.gsonBean;

/**
 * Created by Administrator on 2016/12/12.
 */

public class DataOverview {

    private String imgUri;
    private String titlle;
    private String content;
    private String id;

    public DataOverview(){

    }

    public DataOverview(String id, String content, String titlle, String imgUri) {
        this.id = id;
        this.content = content;
        this.titlle = titlle;
        this.imgUri = imgUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getTitlle() {
        return titlle;
    }

    public void setTitlle(String titlle) {
        this.titlle = titlle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
