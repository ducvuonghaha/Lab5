package com.example.lab5.loader;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetBGLoader extends AsyncTask<String,String,String> {
    WallpaperManager wallpaperManager;
    Context context;
    WindowManager windowManager;
    Bitmap bitmap1,bitmap2;
    DisplayMetrics displayMetrics;
    int height,width;
    public SetBGLoader(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        wallpaperManager  = WallpaperManager.getInstance(context.getApplicationContext());
        String link=strings[0];
        try {
            URL url=new URL(link);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream inputStream=httpURLConnection.getInputStream();
            bitmap1= BitmapFactory.decodeStream(inputStream);
            wallpaperManager.setBitmap(bitmap1);


        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context, "Thay hinh nen thanh cong", Toast.LENGTH_SHORT).show();
    }
}
