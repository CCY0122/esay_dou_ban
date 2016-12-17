package com.example.materialdesigntest.gsonBean;

/**
 * Created by Administrator on 2016/12/12.
 */

public class DataOverview {

    private String imgUri;
    private String title;
    private String content;
    private String id;
    private int start;
    private int total;
    private int count;

    public DataOverview() {

    }

    public DataOverview(String id, String content, String title, String imgUri) {
        this.id = id;
        this.content = content;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
