package com.example.myapplication.controle_de_gastos;

public class Gasto {
    private String name;
    private int image;

    public Gasto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage(){return image;}

    public void serImage(int image) {this.image = image;}

    public void setImage(int image) {
        this.image = image;
    }
}

