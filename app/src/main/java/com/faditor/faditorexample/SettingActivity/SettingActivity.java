package com.faditor.faditorexample.SettingActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.faditor.faditorexample.LoginActivity.LoginActivity;
import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.ProfileEditActivity.ProfileEditActivity;
import com.faditor.faditorexample.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.back).setOnClickListener(onClickListener); //뒤로가기
        findViewById(R.id.setProfile).setOnClickListener(onClickListener); //프로필 수정
        findViewById(R.id.logout).setOnClickListener(onClickListener); //로그아웃
    }
    //로그아웃 함수
    private void signOut() {
        toastMessage("로그아웃");
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        goActivity(LoginActivity.class);
    }
    //회원 탈퇴 함수 : 필요할 때 사용
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back:
                    goActivity(MainActivity.class);
                    break;
                case R.id.setProfile:
                    goActivity(ProfileEditActivity.class);
                    break;
                case R.id.logout:
                    signOut();
                    break;
            }
        }
    };

    private void goActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}