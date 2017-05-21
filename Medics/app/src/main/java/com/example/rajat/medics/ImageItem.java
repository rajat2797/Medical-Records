package com.example.rajat.medics;

import android.graphics.Bitmap;

/**
 * Created by rajat on 26/4/17.
 */

public class ImageItem {
    private Bitmap image;
    private String title;
    private String desc;
    private String date;
//    private int id;

    public ImageItem(Bitmap image, String title, String date, String desc) {
        super();
        this.image = image;
        this.title = title;
        this.date = date;
        this.desc = desc;
//        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    /*public int getId() {
        return id;
    }*/

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
