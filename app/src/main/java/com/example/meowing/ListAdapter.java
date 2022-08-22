package com.example.meowing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Cat> {

    private ArrayList<Cat> catList;

    public ListAdapter(Context context, ArrayList<Cat> catArrayList){

        super(context, R.layout.list_item, catArrayList);
        catList = catArrayList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Cat cat = getItem(position);

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        ImageView imageView = convertView.findViewById(R.id.profilePic);
        TextView textView = convertView.findViewById(R.id.catName);

        Glide.with(getContext()).load(catList.get(position).getImage()).into(imageView);
        textView.setText(cat.Nome);

        return convertView;
    }

    public void addElement(Cat element) {

        catList.add(element);

    }

}