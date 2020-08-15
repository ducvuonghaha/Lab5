package com.example.lab5.activity;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.lab5.EndlessRecyclerViewScrollListener;
import com.example.lab5.R;
import com.example.lab5.adapter.PhotoAdapter;
//import com.example.lab5.adapter.PhotoSearchAdapter;
import com.example.lab5.json_favorites.Example;
import com.example.lab5.json_favorites.Photo;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeLayout;
    private RecyclerView rvPhotos;
    private int page = 1;
    private int pageSearch = 1;
    private PhotoAdapter photoAdapter;
    //    private PhotoSearchAdapter photoSearchAdapter;
    private ArrayList<com.example.lab5.json_favorites.Photo> photoList;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    //    private ArrayList<com.example.lab5.json_search.Photo> photoSearchList;
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


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
        loadPhotos(page);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FavoritesActivity.this.page = 1;
                photoList.clear();
                loadPhotos(FavoritesActivity.this.page);
                photoAdapter.notifyDataSetChanged();
                photoAdapter.notifyItemRangeInserted(0, photoList.size());

            }

        });


        rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                FavoritesActivity.this.page++;
                loadPhotos(FavoritesActivity.this.page);
//                photoAdapter.notifyDataSetChanged();
//                photoAdapter.notifyItemRangeInserted(0, photoList.size());

            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(rvPhotos, dx, dy);
                if (dy > 0) {
                    getSupportActionBar().hide();
//                    Log.e("RecyclerView scrolled: ", "scroll up!");
                } else {
                    getSupportActionBar().show();
//                    Log.e("RecyclerView scrolled: ", "scroll down!");
                }
            }
        });
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public void loadPhotos(int page) {

        AndroidNetworking.post("https://www.flickr.com/services/rest/")
                .addBodyParameter("method", "flickr.favorites.getList")
                .addBodyParameter("api_key", "38d6aedcff4a62c85699b67c1b352a18")
                .addBodyParameter("user_id", "187032707@N07")
                .addBodyParameter("format", "json")
                .addBodyParameter("nojsoncallback", "1")
                .addBodyParameter("extras", "views,media,path_alias,date_taken,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o")
                .addBodyParameter("per_page", "10")
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
                        try {
                            photoList.addAll(photos);
                            photoAdapter.notifyDataSetChanged();
                            photoAdapter.notifyItemRangeInserted(0, photoList.size());

                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_main_menu, menu);
        MenuItem item = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search....");
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String text = newText.toLowerCase().trim();
                if (text.length() == 0) {
                    Toast.makeText(FavoritesActivity.this, "Chưa nhập tên ảnh !", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(FavoritesActivity.this, text, Toast.LENGTH_SHORT).show();
                    photoList = new ArrayList<>();
                    photoAdapter = new PhotoAdapter(FavoritesActivity.this, photoList);
                    staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    rvPhotos.setLayoutManager(staggeredGridLayoutManager);
                    rvPhotos.setAdapter(photoAdapter);
                    rvPhotos.setItemAnimator(null);
                    staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                    rvPhotos.getRecycledViewPool().clear();


                    photoList.clear();
                    pageSearch++;
                    searchImage(pageSearch, text);

                    rvPhotos.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                        @Override
                        public void onLoadMore(int  page, int totalItemsCount, RecyclerView view) {

                            pageSearch++;
                            searchImage(pageSearch++, text);
                            photoAdapter.notifyDataSetChanged();
                            photoAdapter.notifyItemRangeInserted(0, photoList.size());

                        }
                    });
                }
                return true;
            }
        });
        return true;
    }

    private void searchImage(int page, String text) {
        if (text == null) {
            Toast.makeText(this, "Chưa nhập tên ảnh !", Toast.LENGTH_SHORT).show();
        } else {
            AndroidNetworking.post("https://www.flickr.com/services/rest/")
                    .addBodyParameter("method", "flickr.photos.search")
                    .addBodyParameter("api_key", "38d6aedcff4a62c85699b67c1b352a18")
                    .addBodyParameter("tag_mode", "any")
                    .addBodyParameter("text", text)
                    .addBodyParameter("format", "json")
                    .addBodyParameter("nojsoncallback", "1")
                    .addBodyParameter("extras", "views,media,path_alias,date_taken,url_sq,url_t,url_s,url_q,url_m,url_n,url_z,url_c,url_l,url_o")
                    .addBodyParameter("per_page", "10")
                    .addBodyParameter("page", String.valueOf(page))
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(com.example.lab5.json_search.Example.class, new ParsedRequestListener() {
                        @Override
                        public void onResponse(Object response) {

                            swipeLayout.setRefreshing(false);
                            com.example.lab5.json_search.Example example = (com.example.lab5.json_search.Example) response;
                            List<com.example.lab5.json_favorites.Photo> photos = example.getPhotos().getPhoto();
                            Log.e("AAA", photos.get(0).getTitle());

                            try {

                                photoList.addAll(photos);
                                Log.e("BBB", photoList.get(0).getTitle());
                                photoAdapter.notifyDataSetChanged();
                                photoAdapter.notifyItemRangeInserted(0, photoList.size());

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites:
                Intent intent = new Intent(FavoritesActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.galleries:
                Intent intent1 = new Intent(FavoritesActivity.this, GalleriesActivity.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}