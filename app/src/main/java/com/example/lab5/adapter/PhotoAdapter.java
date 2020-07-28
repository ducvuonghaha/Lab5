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
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.activity.DetailActivity;
import com.example.lab5.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Holder> {

    Context context;
    ArrayList<com.example.lab5.json_favorites.Photo> photoList;

    public PhotoAdapter(Context context, ArrayList<com.example.lab5.json_favorites.Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.Holder holder, int position) {
        holder.photo = photoList.get(position);
        holder.tvView.setText(holder.photo.getViews());
        String link = holder.photo.getUrlL();
        Picasso.get().load(link).into(holder.imgList);
        holder.imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DetailActivity.class);
                intent.putExtra("url",holder.photo.getUrlL());
                intent.putExtra("title", holder.photo.getTitle());
                intent.putExtra("views", holder.photo.getViews());
                intent.putExtra("owner", holder.photo.getPathalias());
                intent.putExtra("datetaken", holder.photo.getDatetaken());
                context.startActivity(intent);

            }
        });



        try {
            ConstraintSet constraintSet =new ConstraintSet();

            String imageRatio = String.format("%d:%d",holder.photo.getWidthL(),holder.photo.getHeightL());
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
        private com.example.lab5.json_favorites.Photo photo;


        public Holder(@NonNull View itemView) {
            super(itemView);
            parentConstraint = (ConstraintLayout) itemView.findViewById(R.id.parentConstraint);
            imgList = (ImageView) itemView.findViewById(R.id.imgList);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
        }
    }
}
