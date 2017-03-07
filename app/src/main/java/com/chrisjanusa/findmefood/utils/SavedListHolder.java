package com.chrisjanusa.findmefood.utils;

import com.chrisjanusa.findmefood.models.Restaurant;

import java.util.ArrayList;
import java.util.Random;

/**
 * Singleton design pattern to hold one instance of our savedList throughout the application.
 */
public class SavedListHolder {

    private static SavedListHolder instance = null;
    private static ArrayList<Restaurant> savedList;

    public static SavedListHolder getInstance() {
        if (instance == null) {
            savedList = new ArrayList<>();
            instance = new SavedListHolder();
        }

        return instance;
    }

    public ArrayList<Restaurant> getSavedList() {
        return savedList;
    }

    public void setSavedList(ArrayList<Restaurant> list) {
        savedList = list;
    }

    public Restaurant getRandom(){
        return savedList.get(new Random().nextInt(savedList.size()));
    }

    public boolean isEmpty(){
        return savedList.isEmpty();
    }

}
