package com.example.findflickrrecentphotosapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findflickrrecentphotosapp.model.GalleryItem;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<GalleryItem> mGalleryItems;
    private int mNumberItems;
    private Context context;
    final private ListItemClickListener mOnClickListener;

    private MyHandlerThread<MyAdapter.MyViewHolder> myHandlerThread;

    public interface ListItemClickListener {
        void onListItemClick (int clickedIndex);
    }
    public MyAdapter (List<GalleryItem> galleryItems, int numberOfItems, ListItemClickListener listener, Context context, MyHandlerThread<MyAdapter.MyViewHolder> myHandlerThread){

        mGalleryItems=galleryItems;
        mNumberItems=numberOfItems;
        mOnClickListener=listener;
        this.context=context;
        this.myHandlerThread=myHandlerThread;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.list_item_gallery, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GalleryItem galleryItem=mGalleryItems.get(position);
        Drawable placeholder=context.getResources().getDrawable(R.drawable.minyazev);

        myHandlerThread.queuePhoto(holder,galleryItem.getmUrl());

        holder.bind(placeholder);
    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mItemImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemImageView=itemView.findViewById(R.id.item_image_view);
            mItemImageView.setOnClickListener(this);
        }

        public void bind(Drawable drawable){
            mItemImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            mOnClickListener.onListItemClick(position);

        }
    }
}
