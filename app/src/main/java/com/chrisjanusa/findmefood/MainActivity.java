package com.chrisjanusa.findmefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.chrisjanusa.findmefood.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private FloatingSearchView searchLocationBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.chrisjanusa.findmefood.R.layout.activity_main);
        searchLocationBox = (FloatingSearchView) findViewById(R.id.searchBox);
        mDrawerList = (ListView)findViewById(com.chrisjanusa.findmefood.R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        View header = getLayoutInflater().inflate(com.chrisjanusa.findmefood.R.layout.header, null);
        mDrawerList.addHeaderView(header);
        mDrawerLayout = (DrawerLayout) findViewById(com.chrisjanusa.findmefood.R.id.drawer_layout);
        searchLocationBox.attachNavigationDrawerToMenuButton(mDrawerLayout);

    }

    private void addDrawerItems(){

        String[] thing = {"Favorites","Blocked","History"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, thing);

        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                selectItem(position);
            }
        });

    }

    private  class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            selectItem(arg2);

        }

    }

    private void selectItem(int position) {
        Intent intent;
        switch(position) {
            case 1:
                intent = new Intent(this, SavedListActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, DislikeListActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, HistoryListActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }

    // Function to be called from MainActivityFragment to return the menu button view for the savedList.
    public View getMenuItemView(int id) {
        return findViewById(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Call MainActivityFragment's onRequestPermissionsResult method.
        MainActivityFragment fragment = (MainActivityFragment) this.getSupportFragmentManager().findFragmentById(com.chrisjanusa.findmefood.R.id.mainFragment);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Call MainActivityFragment's onActivityResult method.
        MainActivityFragment fragment = (MainActivityFragment) this.getSupportFragmentManager().findFragmentById(com.chrisjanusa.findmefood.R.id.mainFragment);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
