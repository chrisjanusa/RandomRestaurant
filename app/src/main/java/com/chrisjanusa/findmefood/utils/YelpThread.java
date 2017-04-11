package com.chrisjanusa.findmefood.utils;



import com.chrisjanusa.findmefood.fragments.MainActivityFragment;



public class YelpThread extends Thread {
    String lat;
    String lon;
    String input;
    String filter;
    int whichAsyncTask;
    int offset;

    public YelpThread(String lat, String lon, String input,
                      String filter, int whichAsyncTask, int offset) {
        this.lat = lat;
        this.lon = lon;
        this.input = input;
        this.filter = filter;
        this.whichAsyncTask = whichAsyncTask;
        this.offset = offset;
    }

    public void run() {
        MainActivityFragment.queryYelp(lat, lon, input, filter, offset, whichAsyncTask, this);
    }

}
