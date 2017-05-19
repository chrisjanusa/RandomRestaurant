package com.chrisjanusa.findmefood;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chrisjanusa.findmefood.utils.Item;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Item> {

    ArrayList<Item> itemList = new ArrayList<>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        itemList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.menu_text_image, null);
        TextView textView = (TextView) v.findViewById(R.id.text1);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        CharSequence itemTitle = itemList.get(position).getTitle();
        textView.setText(itemTitle);
        imageView.setImageResource(itemList.get(position).getImage());
        return v;

    }

}