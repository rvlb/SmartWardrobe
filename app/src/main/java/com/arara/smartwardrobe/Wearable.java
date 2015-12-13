package com.arara.smartwardrobe;

public class Wearable {

    String id, colors, type, brand, description;

    public Wearable(String id, String colors, String type, String brand, String description) {
        this.id = id;
        this.colors = colors;
        this.type = type;
        this.brand = brand;
        this.description = description;
    }

    public Wearable(String colors, String type, String brand, String description) {
        this.id = "";
        this.colors = colors;
        this.type = type;
        this.brand = brand;
        this.description = description;
    }
}
