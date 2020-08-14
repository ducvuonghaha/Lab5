package com.example.lab5.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.OnLoadMoreListener;
import com.example.lab5.activity.DetailActivity;
import com.example.lab5.R;
import com.example.lab5.activity.ImageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private Context context;
    private ArrayList<com.example.lab5.json_favorites.Photo> photoList;


    public PhotoAdapter(Context context, ArrayList<com.example.lab5.json_favorites.Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }


//    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
//        this.mOnLoadMoreListener = mOnLoadMoreListener;
//    }

    @NonNull
    @Override
    public PhotoAdapter.PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
            return new PhotoHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder photoHolder, int position) {

            photoHolder.photo = photoList.get(position);
            photoHolder.tvView.setText(photoHolder.photo.getViews());
            String link = photoHolder.photo.getUrlL();
            Picasso.get().load(link).into(photoHolder.imgList);
            photoHolder.imgList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context.getApplicationContext(), DetailActivity.class);
//                    intent.putExtra("url", photoHolder.photo.getUrlL());
//                    intent.putExtra("title", photoHolder.photo.getTitle());
//                    intent.putExtra("views", photoHolder.photo.getViews());
//                    intent.putExtra("owner", photoHolder.photo.getPathalias());
//                    intent.putExtra("datetaken", photoHolder.photo.getDatetaken());
//                    context.startActivity(intent);
                    Intent intent = new Intent(context.getApplicationContext(), ImageActivity.class);
                    intent.putExtra("LIST",photoList);
                    intent.putExtra("POSITION",position);
                    intent.putExtra("title", photoHolder.photo.getTitle());
                    intent.putExtra("views", photoHolder.photo.getViews());
                    intent.putExtra("owner", photoHolder.photo.getPathalias());
                    intent.putExtra("datetaken", photoHolder.photo.getDatetaken());
                    intent.putExtra("UrlHigh",photoHolder.photo.getUrlO());
                    intent.putExtra("UrlMedium",photoHolder.photo.getUrlL());
                    intent.putExtra("UrlLow",photoHolder.photo.getUrlM());

                    context.startActivity(intent);
                }
            });

            try {
                ConstraintSet constraintSet = new ConstraintSet();
                String imageRatio = String.format("%d:%d", photoHolder.photo.getWidthL(), photoHolder.photo.getHeightL());
                constraintSet.clone(photoHolder.parentConstraint);
                constraintSet.setDimensionRatio(photoHolder.imgList.getId(), imageRatio);
                constraintSet.applyTo(photoHolder.parentConstraint);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

    }


    @Override
    public int getItemCount() {
        return photoList.size();
    }


    public class PhotoHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout parentConstraint;
        private ImageView imgList;
        private LinearLayout linearLayout;
        private TextView tvView;
        private com.example.lab5.json_favorites.Photo photo;
//        ProgressBar progressBar;


        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            parentConstraint = (ConstraintLayout) itemView.findViewById(R.id.parentConstraint);
            imgList = (ImageView) itemView.findViewById(R.id.imgList);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
//            progressBar = itemView.findViewById(R.id.progressBar);
        }

    }

}
