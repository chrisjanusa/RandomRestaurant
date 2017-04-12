package com.chrisjanusa.findmefood.utils;



import com.chrisjanusa.findmefood.fragments.MainActivityFragment;

import java.util.concurrent.CountDownLatch;


public class YelpThread extends Thread {
    CountDownLatch latch;
    String lat;
    String lon;
    String input;
    String filter;
    int whichAsyncTask;
    int offset;
    DislikeListHolder dislikeListHolder;

    public YelpThread(String lat, String lon, String input,
                      String filter, int whichAsyncTask, int offset, CountDownLatch latch, DislikeListHolder dislikeListHolder) {
        this.lat = lat;
        this.lon = lon;
        this.input = input;
        this.filter = filter;
        this.whichAsyncTask = whichAsyncTask;
        this.offset = offset;
        this.latch = latch;
        this.dislikeListHolder = dislikeListHolder;
    }

    public void run() {
        MainActivityFragment.queryYelp(lat, lon, input, filter, offset, whichAsyncTask, this, latch, dislikeListHolder);
    }

}
