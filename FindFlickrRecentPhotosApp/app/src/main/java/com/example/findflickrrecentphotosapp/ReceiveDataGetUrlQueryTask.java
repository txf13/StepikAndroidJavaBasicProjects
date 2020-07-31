package com.example.findflickrrecentphotosapp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.findflickrrecentphotosapp.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.URL;

public class ReceiveDataGetUrlQueryTask extends AsyncTask<Object, Void, String> {

    private static final String TAG = "ReceiveDataGetUrlQueryT";

    public AsyncResponse delegate;

    public interface AsyncResponse {
        void proccessFinish (String output);
    }

    public ReceiveDataGetUrlQueryTask(AsyncResponse delegate){
        this.delegate=delegate;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... objects) {
        URL searchURL=(URL)objects[0];
        String searchResults=null;
        try {
            searchResults= NetworkUtilities.getResponseFromHttpGetUrl(searchURL);
            
        } catch (IOException e){
            e.printStackTrace();
        }
        //Log.d(TAG, "doInBackground: searchResults="+searchResults);
        return searchResults;
    }

    @Override
    protected void onPostExecute(String searchResults) {
        if (searchResults != null && !searchResults.equals("")) {
          delegate.proccessFinish(searchResults);
        }
        //super.onPostExecute(s);
    }
}
