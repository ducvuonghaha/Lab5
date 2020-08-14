package com.example.lab5.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.example.lab5.R;
import com.example.lab5.adapter.PhotoAdapter;
import com.example.lab5.json_favorites.Photo;
import com.example.lab5.loader.SetBGLoader;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageActivity extends AppCompatActivity {
    int position;
    //    private ViewPager viewPager;
    private FloatingActionButton fabAction1;
    private FloatingActionButton fabAction2;
    private FloatingActionButton fabAction3;
    private FloatingActionButton fabAction4;
    private FloatingActionButton fabAction5;



    //    private PhotoAdapter fullSizeAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private ImageView imgFullSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image);
        initView();
        imgFullSize = findViewById(R.id.imgFullSize1);
        AndroidNetworking.initialize(getApplicationContext());

        Intent intent = getIntent();

        final ArrayList<Photo> list = (ArrayList<Photo>) intent.getSerializableExtra("LIST");
        position = intent.getIntExtra("POSITION", 0);

        String urlO = intent.getStringExtra("UrlHigh");
        String urlL = intent.getStringExtra("UrlMedium");
        String urlM = intent.getStringExtra("UrlLow");

        Bundle bundle = getIntent().getExtras();

        String url = bundle.getString("UrlMedium");


//        fullSizeAdapter = new PhotoAdapter(this, list);
//        viewPager.setAdapter(fullSizeAdapter);
//        viewPager.setCurrentItem(position, true);

        Picasso.get().load(url).into(imgFullSize);

        fabAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Photo photo = list.get(viewPager.getCurrentItem());
                if (urlO == null) {
                    Toast.makeText(ImageActivity.this, "Ảnh không có kích thước này", Toast.LENGTH_SHORT).show();
                } else {
                    startDownload(urlO);
                }
            }
        });
        fabAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Photo photo = list.get(viewPager.getCurrentItem());
                if (urlL == null) {
                    Toast.makeText(ImageActivity.this, "Ảnh không có kích thước này", Toast.LENGTH_SHORT).show();
                } else {
                    startDownload(urlL);
                }
            }

        });
        fabAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Photo photo = list.get(viewPager.getCurrentItem());
                if (urlM == null) {
                    Toast.makeText(ImageActivity.this, "Ảnh không có kích thước này", Toast.LENGTH_SHORT).show();
                } else {
                    startDownload(urlM);
                }
            }
        });

        String title = intent.getStringExtra("title");
        String views = intent.getStringExtra("views");
        String owner = intent.getStringExtra("owner");
        String datetaken = intent.getStringExtra("datetaken");

        fabAction4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Photo photo = list.get(viewPager.getCurrentItem());
                Bundle bundle = new Bundle();
                Intent intent1 = new Intent(ImageActivity.this, DetailActivity.class);
                intent1.putExtra("title", title);
                intent1.putExtra("owner", owner);
                intent1.putExtra("datetaken", datetaken);
                intent1.putExtra("url", urlL);
                intent1.putExtra("views", views);
                Log.e("title", title);

                bundle.putString("url", urlL);
                intent1.putExtras(bundle);
                try {
                    Pair[] pair = new Pair[1];
                    pair[0] = new Pair<View, String>(imgFullSize, "imageTransition");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation
                            (ImageActivity.this, pair);
                    startActivity(intent1, activityOptions.toBundle());
                } catch (NullPointerException e) {
                    Toast.makeText(ImageActivity.this, "Lỗi: " + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        fabAction5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this,
                        Manifest.permission.SET_WALLPAPER)) {
                    Toast.makeText(ImageActivity.this, "WRITE_EXTERNAL_STORAGE permission allows us to Access SD CARD app", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(ImageActivity.this, new String[]{
                            Manifest.permission.SET_WALLPAPER}, 1234);
                }
                SetBGLoader setBGLoader=new SetBGLoader(ImageActivity.this);
                setBGLoader.execute(url);
            }
        });
    }

    public void startDownload(String url) {
        //Create download request
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                //Allow type of network to download files
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle("Download");
                request.setDescription("GalleryApp");
                request.allowScanningByMediaScanner();
                request.setMimeType("image/jpg");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //get current time for image file
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + ".jpg");

                //get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        } else {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //Allow type of network to download files
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Download");
            request.setDescription("GalleryApp");
            request.allowScanningByMediaScanner();
            request.setMimeType("image/jpg");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //get current time for image file
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, System.currentTimeMillis() + ".jpg");

            //get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private void initView() {
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fabAction1 = (FloatingActionButton) findViewById(R.id.fab_action1);
        fabAction2 = (FloatingActionButton) findViewById(R.id.fab_action2);
        fabAction3 = (FloatingActionButton) findViewById(R.id.fab_action3);
        fabAction4 = (FloatingActionButton) findViewById(R.id.fab_action4);
        fabAction5 = (FloatingActionButton) findViewById(R.id.fab_action5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites:
                Intent intent = new Intent(ImageActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.galleries:
                Intent intent1 = new Intent(ImageActivity.this, GalleriesActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}