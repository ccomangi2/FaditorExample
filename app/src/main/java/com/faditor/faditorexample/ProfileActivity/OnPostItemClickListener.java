package com.faditor.faditorexample.ProfileActivity;

import android.view.View;

interface OnPostItemClickListener {
    public void onItemClick(UserPostAdapter.CustomViewHolder holder, View view, int position);
}
