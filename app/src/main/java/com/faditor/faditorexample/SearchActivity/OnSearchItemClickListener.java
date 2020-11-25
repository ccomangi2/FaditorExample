package com.faditor.faditorexample.SearchActivity;

import android.view.View;

interface OnSearchItemClickListener {
    public void onItemClick(SearchAdapter.CustomViewHolder holder, View view, int position);
}
