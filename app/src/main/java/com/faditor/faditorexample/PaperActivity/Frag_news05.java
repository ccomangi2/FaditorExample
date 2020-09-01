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

public class Frag_news05 extends Fragment {
    private View view;

    private static final String TAG = "Frag_news03";

    public static Frag_news05 newInstance() {
        Frag_news05 frag_news05 = new Frag_news05();

        return frag_news05;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_papernews05, container, false);
        Button news = (Button)view.findViewById(R.id.news5);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.musinsa.com/index.php?m=news&cat=FASHION&uid=43358&fbclid=IwAR1BIdY5ZgqIRno1NETzmgZMRz4oxNzcMEvyfmStx4vO8e-ni4kEmTOxHRE"));
                startActivity(intent);
            }
        });
        return view;
    }
}
