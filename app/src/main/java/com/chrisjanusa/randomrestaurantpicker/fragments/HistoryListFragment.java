package com.chrisjanusa.randomrestaurantpicker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chrisjanusa.RandomRestaurantPicker.R;
import com.chrisjanusa.randomrestaurantpicker.db.HistoryDBHelper;
import com.chrisjanusa.randomrestaurantpicker.models.Restaurant;
import com.chrisjanusa.randomrestaurantpicker.utils.HistoryListHolder;
import com.chrisjanusa.randomrestaurantpicker.views.HistoryListRestaurantCardAdapter;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

/**
 * A fragment containing the HistoryListActivity.
 * Responsible for displaying the last 50 random restaurants generated for the user.
 */
public class HistoryListFragment extends Fragment {

    LinearLayout rootLayout;
    RecyclerView listRecyclerView;
    TextView emptyListView;
    CircularProgressBar progressBar;

    HistoryListRestaurantCardAdapter historyRestaurantCardAdapter;

    HistoryListHolder historyListHolder;
    HistoryDBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        // http://developer.android.com/training/basics/activity-lifecycle/recreating.html
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            historyListHolder.setSavedList(savedInstanceState.<Restaurant>getParcelableArrayList("historyList"));
        }

        this.dbHelper = new HistoryDBHelper(getContext(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        historyListHolder = HistoryListHolder.getInstance();

        rootLayout = (LinearLayout) inflater.inflate(R.layout.fragment_history_list, container, false);
        listRecyclerView = (RecyclerView) rootLayout.findViewById(R.id.listRecyclerView);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyListView = (TextView) rootLayout.findViewById(R.id.emptyText);
        progressBar = (CircularProgressBar) rootLayout.findViewById(R.id.circularProgressBarHistoryList);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                // If user has swiped left, remove the item from the list.
                if (direction == 4) {
                    historyRestaurantCardAdapter.remove(viewHolder.getAdapterPosition());
                }

                // If user has swiped right, open Yelp to current restaurant's page.
                if (direction == 8) {

                    // Open restaurant in Yelp.
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(historyListHolder.getSavedList().get(viewHolder.getAdapterPosition()).getUrl())));

                    // We don't want to remove the list item if user wants to see it in Yelp.
                    // Tell the adapter to refresh so the item is can be visible again.
                    historyRestaurantCardAdapter.notifyDataSetChanged();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(listRecyclerView);

        return rootLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If the list is null or empty, we want to avoid any exceptions thrown from the
        // RecyclerView Adapter. So, set TextView to inform the user no items have been added to the savedList.
        if (historyListHolder.getSavedList() == null || historyListHolder.isEmpty()) {
            listRecyclerView.setVisibility(View.GONE);
            new GetAllFromDBProgress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        // Else, make the list visible.
        showRestaurants();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                if (!historyListHolder.isEmpty()) {
                    historyRestaurantCardAdapter.removeAll();
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // http://developer.android.com/training/basics/activity-lifecycle/recreating.html
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("historyList", historyListHolder.getSavedList());
    }

    private void showRestaurants() {
        emptyListView.setVisibility(View.GONE);
        listRecyclerView.setVisibility(View.VISIBLE);
        historyRestaurantCardAdapter = new HistoryListRestaurantCardAdapter(getContext());
        listRecyclerView.setAdapter(historyRestaurantCardAdapter);
    }

    /**
     * Background task to query database for the restaurants saved by the user.
     */
    private class GetAllFromDBProgress extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            emptyListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ((CircularProgressDrawable) progressBar.getIndeterminateDrawable()).start();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            historyListHolder.setSavedList(dbHelper.getAll());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.progressiveStop();
            progressBar.setVisibility(View.GONE);

            if (!historyListHolder.isEmpty())
                showRestaurants();
            else
                emptyListView.setVisibility(View.VISIBLE);

            super.onPostExecute(aVoid);
        }
    }
}
