package com.example.lab5.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.R;
import com.example.lab5.json_comment.Comment;

import java.util.List;
import java.util.logging.Handler;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {

    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Holder holder, int position) {
        holder.comment = commentList.get(position);
        holder.tvAuthor.setText(Html.fromHtml(holder.comment.getAuthorname()) + ": ");
        holder.tvContent.setText(" " + Html.fromHtml(holder.comment.getContent()));

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView tvContent;
        private Comment comment;
        private TextView tvAuthor;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }
}
