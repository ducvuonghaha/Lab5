package com.example.lab5.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.R;
import com.example.lab5.activity.DetailActivity;
import com.example.lab5.activity.ImageActivity;
import com.example.lab5.activity.ImageGalleriesActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.Holder> {

    Context context;
    ArrayList<com.example.lab5.json_gallery_photo.Photo> photoList;

    public PhotoGalleryAdapter(Context context, ArrayList<com.example.lab5.json_gallery_photo.Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoGalleryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoGalleryAdapter.Holder holder, int position) {
        holder.photo = photoList.get(position);
        holder.tvView.setText(holder.photo.getViews());
        String link = holder.photo.getUrlL();
        Picasso.get().load(link).into(holder.imgList);
        holder.imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context.getApplicationContext(), ImageGalleriesActivity.class);
                intent.putExtra("LIST",photoList);
                intent.putExtra("POSITION",position);
                intent.putExtra("title", holder.photo.getTitle());
                intent.putExtra("views", holder.photo.getViews());
                intent.putExtra("owner", holder.photo.getPathalias());
                intent.putExtra("datetaken", holder.photo.getDatetaken());
                intent.putExtra("UrlHigh",holder.photo.getUrlL());
                intent.putExtra("UrlMedium",holder.photo.getUrlM());
                intent.putExtra("UrlLow",holder.photo.getUrlQ());
                bundle.putString("UrlMedium", holder.photo.getUrlL());
                intent.putExtras(bundle);
                ActivityOptionsCompat activityOptionsCompat= ActivityOptionsCompat
                        .makeSceneTransitionAnimation((Activity) context, holder.imgList, ViewCompat.getTransitionName(holder.imgList));
                context.startActivity(intent, activityOptionsCompat.toBundle());

            }
        });



        try {
            ConstraintSet constraintSet =new ConstraintSet();
            String imageRatio = String.format("%s:%s",holder.photo.getWidthL(),holder.photo.getHeightL());
            constraintSet.clone(holder.parentConstraint);
            constraintSet.setDimensionRatio(holder.imgList.getId(), imageRatio);
            constraintSet.applyTo(holder.parentConstraint);
         } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ConstraintLayout parentConstraint;
        private ImageView imgList;
        private LinearLayout linearLayout;
        private TextView tvView;
        private com.example.lab5.json_gallery_photo.Photo photo;


        public Holder(@NonNull View itemView) {
            super(itemView);
            parentConstraint = (ConstraintLayout) itemView.findViewById(R.id.parentConstraint);
            imgList = (ImageView) itemView.findViewById(R.id.imgList);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
        }
    }
}
