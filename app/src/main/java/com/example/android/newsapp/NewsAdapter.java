package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Adam on 25.06.2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter (Activity context, ArrayList<News> newsArrayList)
    {
        super(context,0,newsArrayList);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
        TextView authorView = (TextView) convertView.findViewById(R.id.author_view);
        TextView categoryView = (TextView) convertView.findViewById(R.id.category_view);
        TextView dateView = (TextView) convertView.findViewById(R.id.date_view);


        titleView.setText(currentNews.getTitle());
        authorView.setText(currentNews.getAuthor());
        categoryView.setText(currentNews.getCategory());
        dateView.setText(currentNews.getDate());

        return convertView;
    }

}
