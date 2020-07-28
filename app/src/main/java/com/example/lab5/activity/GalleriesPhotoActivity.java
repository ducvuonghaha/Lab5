package com.example.lab5.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.lab5.EndlessRecyclerViewScrollListener;
import com.example.lab5.R;
import com.example.lab5.adapter.PhotoGalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class GalleriesPhotoActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvPhotos;
    private int page = 1;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private PhotoGalleryAdapter photoAdapter;
    private ArrayList<com.example.lab5.json_gallery_photo.Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleries_photo);

        rvPhotos = (RecyclerView) findViewById(R.id.rvPhotos1);
        photoList = new ArrayList<>();
        photoAdapter = new PhotoGalleryAdapter(this, photoList);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvPhotos.setAdapter(photoAdapter);
        rvPhotos.setLayoutManager(staggeredGridLayoutManager);
        rvPhotos.setItemAnimator(null);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvPhotos.getRecycledViewPool().clear();
        photoList.clear();

        loadPhoto(page);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout1);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GalleriesPhotoActivity.this.page = 1;
                photoList.clear();
                loadPhoto(GalleriesPhotoActivity.this.page);
            }
        });

        rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                GalleriesPhotoActivity.this.page++;
                loadPhoto(GalleriesPhotoActivity.this.page++);
            }
        });


    }

    public void loadPhoto(int page) {
        Intent intent = getIntent();
        String id = intent.getStringExtra("gallery");
        Log.e("A", id);
        AndroidNetworking.post("https://www.flickr.com/services/rest/")
                .addBodyParameter("method", "flickr.galleries.getPhotos")
                .addBodyParameter("api_key", "38d6aedcff4a62c85699b67c1b352a18")
                .addBodyParameter("gallery_id", id)
                .addBodyParameter("continuation", "0")
                .addBodyParameter("per_page", "30")
                .addBodyParameter("extras", "views,media,path_alias,date_taken,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o")
                .addBodyParameter("page", String.valueOf(page))
                .addBodyParameter("format", "json")
                .addBodyParameter("nojsoncallback", "1")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(com.example.lab5.json_gallery_photo.Example.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {
                        swipeLayout.setRefreshing(false);
                        com.example.lab5.json_gallery_photo.Example example = (com.example.lab5.json_gallery_photo.Example) response;
                        List<com.example.lab5.json_gallery_photo.Photo> photos = example.getPhotos().getPhoto();
                        try {
                            photoList.addAll(photos);
                            photoAdapter.notifyDataSetChanged();
                            photoAdapter.notifyItemRangeRemoved(0, photoList.size());
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


}

