package com.chrisjanusa.findmefood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.chrisjanusa.findmefood.fragments.MainActivityFragment;

public class MainActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ListView images;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.chrisjanusa.RandomRestaurantPicker.R.layout.activity_main);
        mDrawerList = (ListView)findViewById(com.chrisjanusa.RandomRestaurantPicker.R.id.navList);
        addDrawerItems();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        View header = getLayoutInflater().inflate(com.chrisjanusa.RandomRestaurantPicker.R.layout.header, null);
        mDrawerList.addHeaderView(header);
        mDrawerLayout = (DrawerLayout) findViewById(com.chrisjanusa.RandomRestaurantPicker.R.id.drawer_layout);
        button = (ImageButton)findViewById(com.chrisjanusa.RandomRestaurantPicker.R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            });

    }

    private void addDrawerItems(){

        String[] thing = {"Likes","Dislikes"};
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
            /*case 2:
                //Intent b = new Intent(MainActivity.this, Activity2.class);
                //startActivity(b);
                break;*/
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
        MainActivityFragment fragment = (MainActivityFragment) this.getSupportFragmentManager().findFragmentById(com.chrisjanusa.RandomRestaurantPicker.R.id.mainFragment);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Call MainActivityFragment's onActivityResult method.
        MainActivityFragment fragment = (MainActivityFragment) this.getSupportFragmentManager().findFragmentById(com.chrisjanusa.RandomRestaurantPicker.R.id.mainFragment);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
