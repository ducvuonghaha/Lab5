package com.example.lab5.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.lab5.R;
import com.example.lab5.adapter.CommentAdapter;
import com.example.lab5.json_comment.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvOwner;
    private TextView tvViews;
    private TextView tvDate;
    private EditText edtComment;
    private ImageView imvSend;


    private ImageView imvPhoto;
    private RecyclerView rvComment;

    private List<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManager;

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
        rvComment = (RecyclerView) findViewById(R.id.rvComment);
        edtComment = (EditText) findViewById(R.id.edtComment);
        imvSend = (ImageView) findViewById(R.id.imvSend);

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
            tvDate.setText(datee += dateupload);
        }


        Bundle bundle = getIntent().getExtras();

        String url = bundle.getString("url");
        Picasso.get().load(url).into(imvPhoto);

        String id = intent1.getStringExtra("idPhoto");
        commentList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(DetailActivity.this);

        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = edtComment.getText().toString().trim();
                Comment comment1 = new Comment();
                comment1.setAuthorname("Đức Vượng");
                comment1.setContent(comment);
                commentList.add(comment1);
                commentAdapter.notifyDataSetChanged();
            }
        });

        commentAdapter = new CommentAdapter(this, commentList);
        rvComment.setAdapter(commentAdapter);
        rvComment.setLayoutManager(linearLayoutManager);

        loadComment(id);


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

    private void loadComment(String photoID) {
        AndroidNetworking.post("https://www.flickr.com/services/rest/")
                .addBodyParameter("method", "flickr.photos.comments.getList")
                .addBodyParameter("api_key", "38d6aedcff4a62c85699b67c1b352a18")
                .addBodyParameter("photo_id", photoID)
                .addBodyParameter("format", "json")
                .addBodyParameter("nojsoncallback", "1")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(com.example.lab5.json_comment.Example.class, new ParsedRequestListener() {
                    @Override
                    public void onResponse(Object response) {

                        com.example.lab5.json_comment.Example example = (com.example.lab5.json_comment.Example) response;
                        List<Comment> comments = example.getComments().getComment();
                        try {

                            commentList.addAll(comments);
                            commentAdapter.notifyDataSetChanged();
                            commentAdapter.notifyItemRangeInserted(0, commentList.size());

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