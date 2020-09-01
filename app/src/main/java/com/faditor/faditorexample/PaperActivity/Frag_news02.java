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

public class Frag_news02 extends Fragment {
    private View view;

    private static final String TAG = "Frag_news02";

    public static Frag_news02 newInstance() {
        Frag_news02 frag_news02 = new Frag_news02();

        return frag_news02;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_papernews02, container, false);
        Button news = (Button)view.findViewById(R.id.news2);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.musinsa.com/index.php?m=news&cat=FASHION&uid=43322&fbclid=IwAR1DpUN8JdETDHPDHWBAOOT3ccPUbhE77psYHsleXavu4BEAt7seFy86DTM"));
                startActivity(intent);
            }
        });
        return view;
    }
}
