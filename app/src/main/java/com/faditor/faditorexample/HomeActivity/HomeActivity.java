package com.faditor.faditorexample.HomeActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.faditor.faditorexample.ProfileActivity.ProfileActivity;
import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SettingActivity.SettingActivity;

public class HomeActivity extends Fragment {
    private View view;
    // 상단바 메뉴 버튼
    ImageButton button_profile; //프로필
    ImageButton button_setting; //설정

    // 하단바 메뉴 버튼
    ImageButton button_home;    //홈
    ImageButton button_search;  //검색
    ImageButton button_add;     //게시글 추가
    ImageButton button_notice;  //활동
    ImageButton button_paper;   //패션 뉴스, 잡지

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        button_profile = view.findViewById(R.id.profile_button);
        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
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
