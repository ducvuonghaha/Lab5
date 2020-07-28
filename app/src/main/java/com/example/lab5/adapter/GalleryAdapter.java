package com.example.lab5.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.lab5.R;
import com.example.lab5.activity.GalleriesPhotoActivity;
import com.example.lab5.json_favorites.Photo;
import com.example.lab5.json_galleries.ExampleGalleries;
import com.example.lab5.json_galleries.Gallery;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Holder> {

    Context context;
    ArrayList<Gallery> galleryList;

    public GalleryAdapter(Context context, ArrayList<com.example.lab5.json_galleries.Gallery> galleryList) {
        this.context = context;
        this.galleryList = galleryList;
    }

    @NonNull
    @Override
    public GalleryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new GalleryAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.Holder holder, int position) {
        holder.gallery = galleryList.get(position);
        holder.titleGallery.setText(holder.gallery.getTitle().getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GalleriesPhotoActivity.class);
                intent.putExtra("gallery", holder.gallery.getGalleryId());
                context.startActivity(intent);
            }
        });
    }
        @Override
        public int getItemCount () {
            return galleryList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            private TextView titleGallery;
            private com.example.lab5.json_galleries.Gallery gallery;

            public Holder(@NonNull View itemView) {
                super(itemView);
                titleGallery = (TextView) itemView.findViewById(R.id.titleGallery);
            }
        }
    }
