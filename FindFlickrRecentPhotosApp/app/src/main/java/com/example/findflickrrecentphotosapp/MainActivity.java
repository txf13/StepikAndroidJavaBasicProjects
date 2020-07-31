package com.example.findflickrrecentphotosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.findflickrrecentphotosapp.model.GalleryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements ReceiveDataGetUrlQueryTask.AsyncResponse, MyAdapter.ListItemClickListener {

    private static final String TAG = "MainActivity";

    private MyHandlerThread<MyAdapter.MyViewHolder> myHandlerThread;

    private List<GalleryItem> mItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String query="https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=c7e5c1c132198370b0a14311c5112530&format=json&nojsoncallback=1";
        try {
            URL searchURL =new URL (query);
            new ReceiveDataGetUrlQueryTask(this).execute(searchURL);

        } catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void proccessFinish(String output) {

        Log.d(TAG, "proccessFinish: JSON="+output);
        parseJSONResult(output);

        Handler responseHandler=new Handler();

        myHandlerThread=new MyHandlerThread<>(responseHandler);

        myHandlerThread.setPhotoDownloadListener(new MyHandlerThread.MyHandlerThreadListener<MyAdapter.MyViewHolder>() {
            @Override
            public void onPhotoDownloaded(MyAdapter.MyViewHolder target, Bitmap bitmap) {
                Drawable drawable=new BitmapDrawable(getResources(), bitmap);
                target.bind(drawable);
            }
        });

        myHandlerThread.start();
        myHandlerThread.getLooper();

        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(new MyAdapter(mItems, mItems.size(),this,this, myHandlerThread));


    }

    private void parseJSONResult (String jsonString){

        List <GalleryItem> items=new ArrayList<>();
        try {
            JSONObject jsonBody =new JSONObject(jsonString);
            JSONObject photosJSONObject=jsonBody.getJSONObject("photos");
            JSONArray photosJSONArray=photosJSONObject.getJSONArray("photo");

            String farm, id, server, secret, current_url="";

            for(int i=0;i<photosJSONArray.length()&& i<100; i++){
                JSONObject photoJSONObject=photosJSONArray.getJSONObject(i);

                GalleryItem item=new GalleryItem();
                farm=photoJSONObject.getString("farm");
                id=photoJSONObject.getString("id");
                server=photoJSONObject.getString("server");
                secret=photoJSONObject.getString("secret");

                current_url="https://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+".jpg";
                Log.d(TAG, "parseJSONResult: "+i+" current_url:"+current_url);
                item.setmUrl(current_url);
                items.add(item);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        mItems=items;
    }

    @Override
    public void onListItemClick(int clickedIndex) {
        Log.d(TAG, "onListItemClick: index="+clickedIndex);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandlerThread.clearQueue();
        myHandlerThread.quit();
    }
}
