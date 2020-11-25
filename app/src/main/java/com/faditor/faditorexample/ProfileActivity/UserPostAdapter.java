package com.faditor.faditorexample.ProfileActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.R;

import java.util.ArrayList;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.CustomViewHolder> implements OnPostItemClickListener {
    private ImageButton conent_comment;
    private ArrayList<PostData> arrayList;
    private Context context;
    OnPostItemClickListener listener;

    @Override
    public void onItemClick(CustomViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(holder,view,position);
        }
    }

    public void setOnItemClicklistener(OnPostItemClickListener listener){
        this.listener = listener;
    }

    public UserPostAdapter(ArrayList<PostData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPhoto())
                .into(holder.photo_upload);
    }
    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView photo_upload;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.photo_upload = itemView.findViewById(R.id.photo_upload);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(CustomViewHolder.this, v, position);
                    }
                }
            });
        }
    }
    public PostData getItem(int position){ return arrayList.get(position); }
}
