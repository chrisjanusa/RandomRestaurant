package com.chrisjanusa.findmefood.utils;

import android.util.Log;

import com.chrisjanusa.findmefood.models.Restaurant;

import java.util.ArrayList;
import java.util.Random;

/**
 * Singleton design pattern to hold one instance of our savedList throughout the application.
 */
public class HistoryListHolder {

    private static HistoryListHolder instance = null;
    private static ArrayList<Restaurant> savedList;

    public static HistoryListHolder getInstance() {
        if (instance == null) {
            savedList = new ArrayList<>();
            instance = new HistoryListHolder();
        }

        return instance;
    }

    public ArrayList<Restaurant> getSavedList() {
        return savedList;
    }

    public Restaurant get(String name){
        for(Restaurant res: savedList){
            Log.d("RNG", "name = " + name + " res1.name = " + res.getName());
            if(res.isSame(name)){
                return res;
            }
        }
        return null;
    }

    public Restaurant get(int index){
        return savedList.get(index);
    }

    public void setSavedList(ArrayList<Restaurant> list) {
        savedList = list;
    }

    public void add(Restaurant res){
        savedList.add(0, res);
    }

    public int remove(Restaurant res){
        int index = savedList.indexOf(res);
        savedList.remove(res);
        return index;
    }

    public int clear(){
        int size = this.size();
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
