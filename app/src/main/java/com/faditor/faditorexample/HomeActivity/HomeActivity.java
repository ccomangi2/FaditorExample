package com.faditor.faditorexample.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faditor.faditorexample.PaperActivity.PaperActivity;
import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SettingActivity.SettingActivity;

public class HomeActivity extends Fragment {
    private View view;
    // 상단바 메뉴 버튼
    ImageButton button_news; //뉴스
    ImageButton button_setting; //설정


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, null);

        button_news = view.findViewById(R.id.news_button);
        button_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaperActivity.class);
                startActivity(intent);
            }
        });
        button_setting = view.findViewById(R.id.setting_button);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
