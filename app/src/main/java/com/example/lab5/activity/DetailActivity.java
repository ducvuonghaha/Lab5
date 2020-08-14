package com.example.lab5.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lab5.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvOwner;
    private TextView tvViews;
    private TextView tvDate;

    private ImageView imvPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOwner = (TextView) findViewById(R.id.tvOwner);
        tvViews = (TextView) findViewById(R.id.tvViews);
        imvPhoto = (ImageView) findViewById(R.id.imvPhoto);
        tvDate = (TextView) findViewById(R.id.tvDate);

        Intent intent1 = getIntent();
        String ownerr = "Tác giả: ";
        String views = "Lượt xem: ";
        String datee = "Ngày tải lên: ";


        String title = intent1.getStringExtra("title");
        if (title == null) {
            tvTitle.setText("Khong co tieu de");
        } else {
            tvTitle.setText(title);
        }


        String owner = intent1.getStringExtra("owner");
        if (owner == null) {
            tvOwner.setText("Khong co tac gia");
        } else {
            tvOwner.setText(ownerr += owner);
        }

        String viewss = intent1.getStringExtra("views");

        if (views == null) {
            tvViews.setText("Khong co luot xem");
        } else {
            tvViews.setText(views += viewss);
        }

        String dateupload = intent1.getStringExtra("datetaken");
        if (dateupload == null) {
            tvDate.setText("Khong tai duoc ngay");
        } else {
            tvDate.setText(datee +=dateupload );
        }


        Bundle bundle = getIntent().getExtras();

        String url = bundle.getString("url");
        Picasso.get().load(url).into(imvPhoto);
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
                Intent intent = new Intent(DetailActivity.this, FavoritesActivity.class);
                startActivity(intent);
                return true;
            case R.id.galleries:
                Intent intent1 = new Intent(DetailActivity.this, GalleriesActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}