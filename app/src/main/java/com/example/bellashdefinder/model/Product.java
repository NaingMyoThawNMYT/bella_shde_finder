package com.example.bellashdefinder.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String imageUrl;
    private String name;
    private double price;
    private String category;
    private String skinType;
    private String finishFit;
    private String shadeFamily;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public String getFinishFit() {
        return finishFit;
    }

    public void setFinishFit(String finishFit) {
        this.finishFit = finishFit;
    }

    public String getShadeFamily() {
        return shadeFamily;
    }

    public void setShadeFamily(String shadeFamily) {
        this.shadeFamily = shadeFamily;
    }
}
