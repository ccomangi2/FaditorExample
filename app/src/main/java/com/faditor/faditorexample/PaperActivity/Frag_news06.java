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

public class Frag_news06 extends Fragment {
    private View view;

    private static final String TAG = "Frag_news03";

    public static Frag_news06 newInstance() {
        Frag_news06 frag_news06 = new Frag_news06();

        return frag_news06;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_papernews06, container, false);
        Button news = (Button)view.findViewById(R.id.news6);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.musinsa.com/index.php?m=news&cat=FASHION&uid=43313&fbclid=IwAR0CcFJglDfhxPowHdOqvg1NOleEpYwi0k9Wf-Vefc6Pqb_GXYi9sFi73iU"));
                startActivity(intent);
            }
        });
        return view;
    }
}
