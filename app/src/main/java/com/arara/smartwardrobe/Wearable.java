package com.arara.smartwardrobe;

import java.io.Serializable;

public class Wearable implements Serializable {

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

    public Wearable() {
        this.id = "";
        this.colors = "";
        this.type = "";
        this.brand = "";
        this.description = "";
    }

    @Override
    public String toString() {
        return this.brand + "'s " + this.colors + " " + this.type;
    }
}
