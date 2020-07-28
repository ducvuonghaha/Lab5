package com.example.lab5.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.lab5.EndlessRecyclerViewScrollListener;
import com.example.lab5.R;
import com.example.lab5.adapter.GalleryAdapter;
import com.example.lab5.adapter.PhotoAdapter;
import com.example.lab5.json_favorites.Example;
import com.example.lab5.json_favorites.Photo;
import com.example.lab5.json_galleries.ExampleGalleries;
import com.example.lab5.json_galleries.Galleries;
import com.example.lab5.json_galleries.Gallery;

import java.util.ArrayList;
import java.util.List;

public class GalleriesActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeLayout2;
    private RecyclerView rvPhotos2;
    private int page = 1;
    private GalleryAdapter galleryAdapter;
    private ArrayList<Gallery> galleryList;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleries);

            rvPhotos2 = (RecyclerView) findViewById(R.id.rvPhotos2);
            galleryList = new ArrayList<>();
            galleryAdapter = new GalleryAdapter(this, galleryList);
            linearLayoutManager = new LinearLayoutManager(this);
            rvPhotos2.setAdapter(galleryAdapter);
            rvPhotos2.setLayoutManager(linearLayoutManager);
            rvPhotos2.setItemAnimator(null);
            rvPhotos2.getRecycledViewPool().clear();
            galleryList.clear();


        loadGalleries(page);
        swipeLayout2 = (SwipeRefreshLayout) findViewById(R.id.swipeLayout2);
        swipeLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GalleriesActivity.this.page = 1;
                galleryList.clear();
                loadGalleries(GalleriesActivity.this.page);
            }
        });

//        rvPhotos2.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                GalleriesActivity.this.page++;
//                loadGalleries(GalleriesActivity.this.page++);
//            }
//        });
    }


    public void loadGalleries(int page) {

            AndroidNetworking.post("https://www.flickr.com/services/rest/")
                    .addBodyParameter("method", "flickr.galleries.getList")
                    .addBodyParameter("api_key", "38d6aedcff4a62c85699b67c1b352a18")
                    .addBodyParameter("user_id", "187032707@N07")
                    .addBodyParameter("per_page", "10")
                    .addBodyParameter("page", String.valueOf(page))
                    .addBodyParameter("continuation", "0")
                    .addBodyParameter("short_limit", "2")
                    .addBodyParameter("format", "json")
                    .addBodyParameter("nojsoncallback", "1")
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(ExampleGalleries.class, new ParsedRequestListener() {
                        @Override
                        public void onResponse(Object response) {
                            swipeLayout2.setRefreshing(false);
                            ExampleGalleries example = (ExampleGalleries) response;
                            List<Gallery> galleries = example.getGalleries().getGallery();
                            try {
                                galleryList.addAll(galleries);
                                galleryAdapter.notifyDataSetChanged();
                                galleryAdapter.notifyItemRangeRemoved(0, galleryList.size());
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