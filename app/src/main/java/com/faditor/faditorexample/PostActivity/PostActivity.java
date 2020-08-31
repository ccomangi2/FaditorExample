package com.faditor.faditorexample.PostActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.R;

public class PostActivity extends AppCompatActivity {
    ImageButton back;   //뒤로가기
    ImageButton menu;   //게시물 추가, 삭제, 수정 메뉴 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        back = (ImageButton)findViewById(R.id.back);
        menu = (ImageButton)findViewById(R.id.menu);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        //
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
