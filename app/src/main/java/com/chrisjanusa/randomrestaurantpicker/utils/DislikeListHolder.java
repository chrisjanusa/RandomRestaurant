package com.chrisjanusa.randomrestaurantpicker.utils;

import com.chrisjanusa.randomrestaurantpicker.models.Restaurant;

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

    public Restaurant get(String name){
        for(Restaurant res: savedList){
            if(res.isSame(name)){
                return res;
            }
        }
        return null;
    }

    public Restaurant get(int index){
        return savedList.get(index);
    }

    public int clear(){
        int size = this.size();
        savedList.clear();
        return size;
    }

    public int size() {
        return savedList.size();
    }

    public void add(Restaurant res){
        savedList.add(0, res);
    }

    public int remove(Restaurant res){
        int index = savedList.indexOf(res);
        savedList.remove(res);
        return index;
    }

    public void setSavedList(ArrayList<Restaurant> list) {
        savedList = list;
    }

    public boolean resIsContained(Restaurant res){
        for(Restaurant res2: savedList){
            if(res2.isSame(res)){
                return true;
            }
        }
        return false;
    }
}
