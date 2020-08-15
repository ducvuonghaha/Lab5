package com.example.lab5.loader;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lab5.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class SetBGLoader extends AsyncTask<String,String,String> {
    WallpaperManager wallpaperManager;
    Context context;
    private AlertDialog progressDialog;
    Bitmap bitmap1;
    public SetBGLoader(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog = new SpotsDialog(context, "Đang cài hình nền");
        progressDialog.show();

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
        if (progressDialog.isShowing())
            progressDialog.dismiss();
            Toasty.success(context, "Thay hình nền thành công", Toast.LENGTH_SHORT).show();

    }
}
