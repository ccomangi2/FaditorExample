package com.faditor.faditorexample.HomeActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.R;

import java.util.ArrayList;

public class FaditorAdapter extends RecyclerView.Adapter<FaditorAdapter.CustomViewHolder> {
    private ImageButton conent_comment;
    private ArrayList<UserFaditorData> arrayList;
    private Context context;

    public FaditorAdapter(ArrayList<UserFaditorData> arrayList, Context context/*, OnItemClickListener onItemClickListener*/) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_faditor_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPhotoUri())
                .into(holder.account_iv_profile);
        holder.user_name.setText(arrayList.get(position).getUser_name());

    }
    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView account_iv_profile;
        TextView user_name;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.account_iv_profile = itemView.findViewById(R.id.account_iv_profile);
            this.user_name = itemView.findViewById(R.id.user_name);
        }
    }
}
