package com.example.movie.moviefilm;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MovieAdapterReview extends ArrayAdapter<Movie> {
    ArrayList<Movie> list;
    LayoutInflater vi;
    int Resource;
    Context context;
    public MovieAdapterReview(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        list = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v==null)
            v = vi.inflate(Resource, null);
        Movie movie=list.get(position);
        TextView tx_Review=(TextView)v.findViewById(R.id.D_textViewReview);
        TextView tx_Review_author=(TextView)v.findViewById(R.id.textViewReview);
        tx_Review.setText(movie.getReview());
        tx_Review_author.setText(movie.getAuthor());
        Linkify.addLinks(tx_Review, Linkify.WEB_URLS);
        return v;

    }
}
