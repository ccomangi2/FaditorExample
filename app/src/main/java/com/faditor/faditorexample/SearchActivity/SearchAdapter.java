package com.faditor.faditorexample.SearchActivity;

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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomViewHolder> implements OnSearchItemClickListener{
    private ImageButton conent_comment;
    private ArrayList<UserFaditorData> arrayList;
    private Context context;
    OnSearchItemClickListener listener;

    @Override
    public void onItemClick(CustomViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(holder,view,position);
        }
    }

    public void setOnItemClicklistener(OnSearchItemClickListener listener){
        this.listener = listener;
    }


    public SearchAdapter(ArrayList<UserFaditorData> arrayList, Context context/*, OnItemClickListener onItemClickListener*/) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_item, parent, false);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(SearchAdapter.CustomViewHolder.this, v, position);
                    }
                }
            });
        }

    }
    public UserFaditorData getItem(int position){ return arrayList.get(position); }
}
