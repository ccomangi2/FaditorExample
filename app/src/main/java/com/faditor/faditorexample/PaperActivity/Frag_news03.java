package com.faditor.faditorexample.PaperActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faditor.faditorexample.R;

public class Frag_news03 extends Fragment {
    private View view;

    private static final String TAG = "Frag_news03";

    public static Frag_news03 newInstance() {
        Frag_news03 frag_news03 = new Frag_news03();

        return frag_news03;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_papernews03, container, false);
        Button news = (Button)view.findViewById(R.id.news3);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.musinsa.com/index.php?m=news&cat=FASHION&p=2&uid=43261&fbclid=IwAR1C2Q9XvqvkAWs4UQ_S_No9WQaYEqn6gYzeeQbTTFk1ZPy6_ofQ0gSgKoI"));
                startActivity(intent);
            }
        });
        return view;
    }
}
