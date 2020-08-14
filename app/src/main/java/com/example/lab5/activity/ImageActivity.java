package com.example.lab5.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.example.lab5.R;
import com.example.lab5.adapter.FullSizeAdapter;
import com.example.lab5.json_favorites.Photo;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    int position;
    private ViewPager viewPager;
    private FloatingActionButton fabAction1;
    private FloatingActionButton fabAction2;
    private FloatingActionButton fabAction3;
    private FloatingActionButton fabAction4;
    private FullSizeAdapter fullSizeAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
        AndroidNetworking.initialize(getApplicationContext());

        Intent intent = getIntent();


        final ArrayList<Photo> list = (ArrayList<Photo>) intent.getSerializableExtra("LIST");
        position = intent.getIntExtra("POSITION", 0);
        fullSizeAdapter = new FullSizeAdapter(this, list);
        viewPager.setAdapter(fullSizeAdapter);
        viewPager.setCurrentItem(position, true);

        fabAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                if (photo.getUrlO() != null) {
                    startDownload(photo.getUrlO());
                } else {
                    startDownload(photo.getUrlL());
                }
            }
        });
        fabAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                startDownload(photo.getUrlL());
            }

        });
        fabAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                startDownload(photo.getUrlM());

            }
        });
        fabAction4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(viewPager.getCurrentItem());
                Intent intent1 = new Intent(ImageActivity.this, DetailActivity.class);
                intent1.putExtra("title", photo.getTitle());
                intent1.putExtra("owner", photo.getPathalias());
                intent1.putExtra("datetaken", photo.getDatetaken());
                intent1.putExtra("url", photo.getUrlL());
                intent1.putExtra("views", photo.getViews());
                Log.e("title", photo.getTitle());
                startActivity(intent1);
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
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fabAction1 = (FloatingActionButton) findViewById(R.id.fab_action1);
        fabAction2 = (FloatingActionButton) findViewById(R.id.fab_action2);
        fabAction3 = (FloatingActionButton) findViewById(R.id.fab_action3);
        fabAction4 = (FloatingActionButton) findViewById(R.id.fab_action4);

    }
}