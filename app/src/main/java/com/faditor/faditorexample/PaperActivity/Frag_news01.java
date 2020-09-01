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

public class Frag_news01 extends Fragment {
    private View view;

    private static final String TAG = "Frag_news01";

    public static Frag_news01 newInstance() {
        Frag_news01 frag_news01 = new Frag_news01();

        return frag_news01;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_papernews01, container, false);
        Button news = (Button)view.findViewById(R.id.news1);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.musinsa.com/index.php?m=news&cat=FASHION&p=2&uid=43281&fbclid=IwAR1KN2N3zUA6o0DMYafhAuykYUSLmsfH5FAZAEli1bRK874yYllI53si9_k"));
                startActivity(intent);
            }
        });
        return view;
    }
}
