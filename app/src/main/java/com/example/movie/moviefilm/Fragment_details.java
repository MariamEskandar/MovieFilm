package com.example.movie.moviefilm;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MINA on 24/01/2016.
 */
public class Fragment_details extends Fragment {
    ArrayList<Movie> MovieList;
    MovieAdapterDetails adapter;
    ArrayList<Movie> MovieListReview;
    MovieAdapterReview adapterReview;

    ArrayList<Movie> MovieListTrailer;
    MovieAdapterTrailer adapterTrailer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        MovieList = new ArrayList<Movie>();  //f/or detatails top
        MovieListReview = new ArrayList<Movie>();  ///for review
        MovieListTrailer = new ArrayList<Movie>();  ///for trailer
        /////////////////////////////////////////
        Movie movie=new Movie();
        Bundle arguments = getArguments();
        if (arguments != null)
        {
            final int movieID = getArguments().getInt("MovieID");
            String original_title =  getArguments().getString("original_title");
            String image =  getArguments().getString("poster_path");
            String overview= getArguments().getString("overview");
            int vote_average=  getArguments().getInt("vote_average");
            String release_date= getArguments().getString("release_date");
            movie.setId(movieID);
            movie.setImage(image);
            movie.setOriginal_title(original_title);
            movie.setOverview(overview);
            movie.setVote_average(vote_average);
            movie.setRelease_date(release_date);
            MovieList.add(movie);
            System.out.println("arg"+getArguments().getInt("MovieID"));
           final ListView listView=(ListView)view.findViewById(R.id.listViewDetails);
            adapter = new MovieAdapterDetails(getActivity(), R.layout.details_items_list_view, MovieList);
            adapter.notifyDataSetChanged();
             listView.setAdapter(adapter);
            /////////////////////////////////////////////////////////////
            /////For review
            // int movieID = intent.getIntExtra("MovieID", -1);
            new JSONAsyncTask().execute("http://api.themoviedb.org/3/movie/" + movieID + "/reviews?api_key=610d2a960011e9203aaf2547007cd5f9");

            final ListView listViewReview=(ListView)view.findViewById(R.id.listViewReview);
            adapterReview = new MovieAdapterReview(getActivity(), R.layout.item_listview_review, MovieListReview);
            adapterReview.notifyDataSetChanged();
            listViewReview.setAdapter(adapterReview);
////////////////////////
            ///for trailer
            new JSONAsyncTaskTailer().execute("http://api.themoviedb.org/3/movie/" + movieID + "/videos?api_key=610d2a960011e9203aaf2547007cd5f9");
            final ListView listViewTrailer=(ListView)view.findViewById(R.id.listViewTrailer);
            adapterTrailer = new MovieAdapterTrailer(getActivity(), R.layout.item_trailer_listview, MovieListTrailer);
            adapterTrailer.notifyDataSetChanged();
            listViewTrailer.setAdapter(adapterTrailer);

            System.out.println("http://api.themoviedb.org/3/movie/" + movieID + "/videos?api_key=610d2a960011e9203aaf2547007cd5f9");
////////////////////////////////////
            listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long id) {

                    Go_to_Link_Youtube(MovieListTrailer.get(position).getTrailer());
                    //  Toast.makeText(getApplicationContext(), MovieListTrailer.get(position).getTrailer(), Toast.LENGTH_LONG).show();
                }
            });

            ////////for button like in movie details for save move id

            Button b=(Button)view.findViewById(R.id.buttonLike);
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(Details_Movie.this, "Movie_id" + movieID, Toast.LENGTH_LONG).show();
                    SharedPreferences ss = getActivity().getSharedPreferences("db", 0);
                    Set<String> hs = ss.getStringSet("set", new HashSet<String>());
                    hs.add(String.valueOf(movieID));
                    SharedPreferences.Editor edit = ss.edit();
                    edit.putStringSet("set", hs);
                    edit.commit();

                }
            });



        } else {
            Toast.makeText(getActivity().getApplicationContext(), "arguments returns null", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void Go_to_Link_Youtube(String Link)
    {

        Intent intent11 = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        this.startActivity(intent11);


    }
    ///for review
    class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("results");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Movie movie=new Movie();
                        movie.setAuthor(object.getString("author"));
                        movie.setReview(object.getString("content"));
                        MovieListReview.add(movie);
                    }

                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapterReview.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

    class JSONAsyncTaskTailer extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {

                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);


                    JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = jsono.getJSONArray("results");

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        Movie movie=new Movie();
                        movie.setTrailer("https://www.youtube.com/watch?v="+object.getString("key"));

                        System.out.println("keypp" + movie.getTrailer());
                        MovieListTrailer.add(movie);

                    }

                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapterTrailer.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }





}
