package com.faditor.faditorexample.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.CustomViewHolder> {

    private ArrayList<PostData> arrayList;
    private Context context;


    public PostAdapter(ArrayList<PostData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_main_post_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getUserprofileimage())
                .into(holder.account_iv_profile);
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPhoto())
                .into(holder.photo_upload);
        holder.user_name.setText(arrayList.get(position).getUsername());
        holder.post_date.setText(arrayList.get(position).getPostDate());
        holder.text_upload.setText(arrayList.get(position).getContents());
        int start = arrayList.get(position).getStarCount();
        if(holder.content_heart_button.isChecked()) {
            start += 1;
            holder.like_people.setText(String.valueOf(start));
        } else {
            start -= 1;
            holder.like_people.setText(String.valueOf(start));
        }
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView account_iv_profile;
        ImageView photo_upload;
        TextView user_name;
        TextView post_date;
        TextView text_upload;
        TextView like_people;
        CheckBox content_heart_button;
        ImageButton conent_comment;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.account_iv_profile = itemView.findViewById(R.id.account_iv_profile);
            this.photo_upload = itemView.findViewById(R.id.photo_upload);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.post_date = itemView.findViewById(R.id.post_date);
            this.text_upload = itemView.findViewById(R.id.text_upload);
            this.like_people = itemView.findViewById(R.id.like_people);
            this.content_heart_button = itemView.findViewById(R.id.content_heart_button);
            this.conent_comment = itemView.findViewById(R.id.conent_comment);
        }
    }
}
