package com.faditor.faditorexample.PaperActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.R;

public class PaperActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;

    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //뷰페이저 세팅
        viewPager2 = findViewById(R.id.viewPager);
        ViewPager2Adapter adapter = new ViewPager2Adapter(this);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager2.setAdapter(adapter);
    }
}
