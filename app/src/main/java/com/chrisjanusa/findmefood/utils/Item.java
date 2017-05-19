package com.chrisjanusa.findmefood.utils;

/**
 * Created by chrisjanusa on 5/11/17.
 */

public class Item {
    String title;
    int image;

    public Item(String title,int image)
    {
        this.image=image;
        this.title=title;
    }
    public String getTitle()
    {
        return title;
    }
    public int getImage()
    {
        return image;
    }
}
