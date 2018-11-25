package com.example.tiget.musicplayer;

import java.io.Serializable;

public class CategoryConstructor implements Serializable {
    String CategoryName;
    int CategoryBackground;


    public CategoryConstructor(String CategoryName, int CategoryBackground){
        this.CategoryName = CategoryName;
        this.CategoryBackground = CategoryBackground;

    }


    public String getCategoryName(){
        return CategoryName;
    }

    public int getCategoryBackground(){
        return CategoryBackground;
    }


}