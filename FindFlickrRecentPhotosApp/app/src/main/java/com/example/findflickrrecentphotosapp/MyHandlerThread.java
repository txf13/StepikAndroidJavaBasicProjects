package com.example.findflickrrecentphotosapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.findflickrrecentphotosapp.utilities.NetworkUtilities;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MyHandlerThread <T> extends HandlerThread {

    private static final String TAG = "MyHandlerThread";
    private static final int MESSAGE_DOWNLOAD=0;

    private Handler mRequestHandler;

    private ConcurrentMap<T,String> mRequestMap=new ConcurrentHashMap<>();

    private Handler mResponseHandler;

    public MyHandlerThreadListener <T> mMyHandlerThreadListener;

    public interface  MyHandlerThreadListener <T>{
        void onPhotoDownloaded (T target, Bitmap bitmap);
    }

    public void setPhotoDownloadListener (MyHandlerThreadListener<T> listener){
        mMyHandlerThreadListener=listener;
    }

    @Override
    protected void onLooperPrepared() {
//        super.onLooperPrepared();
        mRequestHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
//                super.handleMessage(msg);
                if(msg.what==MESSAGE_DOWNLOAD){
                    T target=(T) msg.obj;
                    Log.i(TAG, "handleMessage: "+mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    private void handleRequest(final T target){
        try{
            final String url=mRequestMap.get(target);
            if(url==null){
                Log.i(TAG, "handleRequest: url=null!");
                return;
            }
            byte[] bitmapBytes = NetworkUtilities.getUrlBytes(url);
            final Bitmap bitmap= BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
            Log.i(TAG, "handleRequest: Bitmap Created");

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mRequestMap.get(target)!=url){
                        return;
                    }
                    mRequestMap.remove(target);
                    mMyHandlerThreadListener.onPhotoDownloaded(target,bitmap);
                }
            });

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public MyHandlerThread(Handler responseHandler){
        super(TAG);
        mResponseHandler=responseHandler;
    }

    public void queuePhoto(T target, String url){
        Log.d(TAG, "queuePhoto: url="+url);
        if(url==null){
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD,target).sendToTarget();
        }

    }

    public void clearQueue(){
        mResponseHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

}
