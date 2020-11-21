package com.faditor.faditorexample.ProfileEditActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faditor.faditorexample.Database.UserData;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.FashionPickActivity.FashionPickActivity;
import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SettingActivity.SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileEditActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    //개인정보 edittext
    EditText b_name, b_email;

    //faditor 정보
    EditText user_name;
    EditText user_intro;
    TextView like_fashion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileedit);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Users").document(user.getUid());

        findViewById(R.id.back).setOnClickListener(onClickListener);
        findViewById(R.id.ok).setOnClickListener(onClickListener);
        findViewById(R.id.fashion_choise_btn).setOnClickListener(onClickListener);

        //개인정보
        b_name = findViewById(R.id.name_edit);
        b_email = findViewById(R.id.email_edit);
        docRef.collection("Basic").document("info").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        b_name.setText(document.getData().get("name").toString());
                        if(document.getData().get("email") != null) {
                            b_email.setText(document.getData().get("email").toString());
                        }
                        if(document.getData().get("email") == null) {
                            b_email.setHint("email");
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        //faditor 정보
        user_name = findViewById(R.id.username_edit);
        user_intro = findViewById(R.id.info_edit);
        like_fashion = findViewById(R.id.like_fashion);
        docRef.collection("Faditor").document("info").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        user_name.setText(document.getData().get("user_name").toString());
                        user_intro.setText(document.getData().get("user_intro").toString());
                        like_fashion.setText(document.getData().get("like_fashion").toString());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //뒤로가기
                    gotomain(MainActivity.class);
                    break;
                case R.id.ok: //수정완료
                    storageUpload();
                    break;
                case R.id.fashion_choise_btn: //패션 분야 선택
                    gotomain(FashionPickActivity.class);
                    break;
            }
        }
    };

    private void storageUpload() {
        //패디터 정보
        String userId = mAuth.getUid();
        String name = user_name.getText().toString();
        String intro = user_intro.getText().toString();
        String fashion = like_fashion.getText().toString();

        //개인정보
        String basic_name = b_name.getText().toString();
        String basic_email = b_email.getText().toString();

        Log.v("알림", "현재로그인한 유저 " + userId);
        Log.v("알림", "사용자 이름 " + name);
        Log.v("알림", "한줄 소개 " + intro);
        Log.v("알림", "관심 패션 분야 " + fashion);

        //final String date_tv = time;
        if(user_name.length() > 0 && b_name.length() > 0 && b_email.length() > 0) {
            UserFaditorData userFaditorData = new UserFaditorData(name, intro, fashion);
            storeUpload(userFaditorData);
            UserData userdata = new UserData(basic_name, basic_email);
            profileUpload(userdata);
            gotomain(SettingActivity.class);
        } else if(user_name.length() == 0 && b_name.length() > 0 && b_email.length() > 0){
            toastMessage("사용자 이름을 입력해 주세요.");
        } else if(user_name.length() > 0 && b_name.length() > 0 && b_email.length() == 0){
            toastMessage("이메일을 입력해 주세요.");
        } else if(user_name.length() > 0 && b_name.length() == 0 && b_email.length() > 0){
            toastMessage("이름을 입력해 주세요.");
        } else {
            toastMessage("입력란을 확인해 주세요.");
        }
    }
    private void storeUpload(UserFaditorData userFaditorData) {
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(user.getUid()).collection("Faditor").document("info").set(userFaditorData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("알림", "DocumentSnapshot added with ID: " + d.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("알림", "Error adding document", e);
                    }
                });
    }
    private void profileUpload(UserData userdata) {
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(user.getUid()).collection("Basic").document("info").set(userdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("알림", "DocumentSnapshot added with ID: " + d.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("알림", "Error adding document", e);
                    }
                });
    }

    private void gotomain(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
