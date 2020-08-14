package com.example.lab5.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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


        String url = intent1.getStringExtra("url");
        Picasso.get().load(url).into(imvPhoto);
    }
}