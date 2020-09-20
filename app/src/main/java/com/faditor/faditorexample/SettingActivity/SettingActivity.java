package com.faditor.faditorexample.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.ProfileEditActivity.ProfileEditActivity;
import com.faditor.faditorexample.R;
import com.google.firebase.auth.FirebaseAuth;


public class SettingActivity extends AppCompatActivity {
    ImageButton back;
    Button setProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 뒤로 가기
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 프로필 수정 페이지 이동
        setProfile = (Button)findViewById(R.id.setProfile);
        setProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });
    }
    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }
}