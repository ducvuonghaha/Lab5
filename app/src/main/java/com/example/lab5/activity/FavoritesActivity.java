package com.example.lab5.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.lab5.EndlessRecyclerViewScrollListener;
import com.example.lab5.R;
import com.example.lab5.adapter.PhotoAdapter;
import com.example.lab5.json_favorites.Example;
import com.example.lab5.json_favorites.Photo;
import com.example.lab5.json_galleries.ExampleGalleries;
import com.example.lab5.json_galleries.Gallery;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvPhotos;
    private int page = 1;
    private PhotoAdapter photoAdapter;
    private ArrayList<com.example.lab5.json_favorites.Photo> photoList;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(FavoritesActivity.this, GalleriesActivity.class);
        switch (item.getItemId()) {
            case R.id.animals:
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
                                ExampleGalleries exampleGalleries = (ExampleGalleries) response;
                                List<Gallery> galleryList = exampleGalleries.getGalleries().getGallery();
                                String id = galleryList.get(0).getGalleryId();
                                Log.e("A", id);
                                intent.putExtra("animal", id);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
                return true;
            case R.id.cars:
                startActivity(intent);
                return true;
            case R.id.girls:
                startActivity(intent);
                return true;
            case R.id.views:
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        try {
            rvPhotos = (RecyclerView) findViewById(R.id.rvPhotos);
            photoList = new ArrayList<>();
            photoAdapter = new PhotoAdapter(this, photoList);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            rvPhotos.setAdapter(photoAdapter);
            rvPhotos.setLayoutManager(staggeredGridLayoutManager);
            rvPhotos.setItemAnimator(null);
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            rvPhotos.getRecycledViewPool().clear();
            photoList.clear();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        loadPhotos(page);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FavoritesActivity.this.page = 1;
                photoList.clear();
                loadPhotos(FavoritesActivity.this.page);
            }
        });

        rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                FavoritesActivity.this.page++;
                loadPhotos(FavoritesActivity.this.page++);
            }
        });
    }

    public void loadPhotos(int page) {
            try {
            AndroidNetworking.post("https://www.flickr.com/services/rest/")
                    .addBodyParameter("method", "flickr.favorites.getList")
                    .addBodyParameter("api_key", "38d6aedcff4a62c85699b67c1b352a18")
                    .addBodyParameter("user_id", "187032707@N07")
                    .addBodyParameter("format", "json")
                    .addBodyParameter("nojsoncallback", "1")
                    .addBodyParameter("extras", "views,media,path_alias,date_taken,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o")
                    .addBodyParameter("per_page", "30")
                    .addBodyParameter("page", String.valueOf(page))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(Example.class, new ParsedRequestListener() {
                        @Override
                        public void onResponse(Object response) {

                            swipeLayout.setRefreshing(false);
                            Example example = (Example) response;
                            List<Photo> photos = example.getPhotos().getPhoto();

                            photoList.addAll(photos);
                            photoAdapter.notifyDataSetChanged();
                            photoAdapter.notifyItemRangeRemoved(0, photoList.size());

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

    }


}