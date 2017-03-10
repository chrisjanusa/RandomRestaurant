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

    public void add(Restaurant res){
        savedList.add(0, res);
    }

    public int remove(Restaurant res){
        int index = savedList.indexOf(res);
        savedList.remove(res);
        return index;
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
        int size = size();
        savedList.clear();
        return size;
    }

    public int size() {
        return savedList.size();
    }

    public boolean resIsContained(Restaurant res){
        for(Restaurant res2: savedList){
            if(res2.isSame(res)){
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        return savedList.isEmpty();
    }

}
