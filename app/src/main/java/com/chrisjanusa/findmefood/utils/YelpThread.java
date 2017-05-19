package com.chrisjanusa.findmefood.utils;



import com.chrisjanusa.findmefood.fragments.MainActivityFragment;

import java.util.concurrent.CountDownLatch;


public class YelpThread extends Thread {
    CountDownLatch latch;
    CountDownLatch errorLatch;
    String lat;
    String lon;
    String input;
    String filter;
    int whichAsyncTask;
    int offset;
    DislikeListHolder dislikeListHolder;

    public YelpThread(String lat, String lon, String input,
                      String filter, int whichAsyncTask, int offset, CountDownLatch latch, DislikeListHolder dislikeListHolder, CountDownLatch errorLatch) {
        this.lat = lat;
        this.lon = lon;
        this.input = input;
        this.filter = filter;
        this.whichAsyncTask = whichAsyncTask;
        this.offset = offset;
        this.latch = latch;
        this.dislikeListHolder = dislikeListHolder;
        this.errorLatch = errorLatch;
    }

    public void run() {
        if(MainActivityFragment.queryYelp(lat, lon, input, filter, offset, whichAsyncTask, this, dislikeListHolder)){
            latch.countDown();
        }
        else{
            errorLatch.countDown();
            try {
                errorLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

}
