package com.example.penBugsBlogApp.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.penBugsBlogApp.Activities.DetailedPostView;
import com.example.penBugsBlogApp.Models.Post;
import com.example.penBugsBlogApp.R;

import java.util.List;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdaptor(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public PostAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.post_view, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdaptor.MyViewHolder holder, int position) {

        holder.postHeader.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // This method is used to show the Post from the database
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView postHeader;
        ImageView imgPost;


         public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            postHeader = itemView.findViewById(R.id.postTextHeader);
            imgPost  = itemView.findViewById(R.id.postImage);

            //this lister is used to show r open the detailed view of the Post
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callDetailedView = new Intent(mContext, DetailedPostView.class);
                    int position = getAdapterPosition();

                    callDetailedView.putExtra("title", mData.get(position).getTitle());
                    callDetailedView.putExtra("picture", mData.get(position).getPicture());
                    callDetailedView.putExtra("description", mData.get(position).getDescription());

                    mContext.startActivity(callDetailedView);
                }
            });
        }
    }
}
