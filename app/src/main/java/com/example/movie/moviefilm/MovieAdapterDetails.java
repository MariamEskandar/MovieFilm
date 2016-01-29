package com.example.movie.moviefilm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



/**
 * Created by MINA on 20/01/2016.
 */
public class MovieAdapterDetails extends ArrayAdapter<Movie> {

    ArrayList<Movie> list;
    LayoutInflater vi;
    int Resource;
    private static int incrementedValue = 0;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String FOLLOWED_ROUTES = "followedRoutes";


    public MovieAdapterDetails(Context context, int resource, ArrayList<Movie> objects) {
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
        final Movie movie=list.get(position);

        TextView textView_original_title = (TextView)v.findViewById(R.id.D_textView_original_title);
        ImageView imageView_d = (ImageView) v.findViewById(R.id.imageView_detail);
        TextView tx_overview=(TextView)v.findViewById(R.id.D_textView_overview);
        TextView tx_vote_average=(TextView)v.findViewById(R.id.D_textView_vote_average);
        TextView tx_release_date=(TextView)v.findViewById(R.id.D_textView_release_date);
        RatingBar ratingBar=(RatingBar)v.findViewById(R.id.ratingBar);


        textView_original_title.setText(movie.getOriginal_title());
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185//" + list.get(position).getImage()).placeholder(R.drawable.ic_launcher).into(imageView_d);
        tx_overview.setText(movie.getOverview());
        tx_vote_average.setText(String.valueOf(movie.getVote_average())+"  Rating");
        tx_release_date.setText(movie.getRelease_date());
        ratingBar.setRating(movie.getVote_average());
        return v;
        // image.setImageResource(Integer.parseInt(m.getImageUrl()));
    /*      title.setText(movie.getTitle());
        desc.setText(m.getDescription());
        date.setText(String.valueOf(m.getYear()));
*/

    }
}
