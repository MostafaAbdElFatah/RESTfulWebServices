package com.mostafa.fci.restfulwebservices;

import android.graphics.Bitmap;

/**
 * Created by FCI on 2018-06-25.
 */

public class Flower {

    private int id;
    private String name;
    private String category;
    private String instructions;
    private String price;
    private String photo;
    private Bitmap bitmap;

    public Flower(){

    }

    public Flower(int id, String name) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.instructions = instructions;
        this.price = price;
        this.photo = photo;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
