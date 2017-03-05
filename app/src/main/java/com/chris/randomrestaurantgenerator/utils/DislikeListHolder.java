package com.chris.randomrestaurantgenerator.utils;

import com.chris.randomrestaurantgenerator.models.Restaurant;

import java.util.ArrayList;

/**
 * Singleton design pattern to hold one instance of our savedList throughout the application.
 */
public class DislikeListHolder {

    private static DislikeListHolder instance = null;
    private static ArrayList<Restaurant> savedList;

    public static DislikeListHolder getInstance() {
        if (instance == null) {
            savedList = new ArrayList<>();
            instance = new DislikeListHolder();
        }

        return instance;
    }

    public ArrayList<Restaurant> getSavedList() {
        return savedList;
    }

    public void setSavedList(ArrayList<Restaurant> list) {
        savedList = list;
    }
}
