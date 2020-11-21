package com.faditor.faditorexample.ProfileEditActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.faditor.faditorexample.Database.UserData;
import com.faditor.faditorexample.Database.UserFaditorData;
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

    //패션 분야 선택 레이아웃
    ConstraintLayout constraintLayout;

    Button vintige; //빈티지
    Button caejual; //캐주얼
    Button dendi; //댄디
    Button clrecik; //클래식
    Button abanggard; //아방가르드
    Button amecagi; //아메카지
    Button strit; //스트릿
    Button spoti; //스포티
    Button pungk; //펑크
    Button modan; //모던
    Button minimal; //미니멀
    Button militari; //밀리터리
    Button leiad; //레이어드
    Button letro; //레트로
    Button feminin; //페미닌
    Button denim; //데님

    String favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileedit);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Users").document(user.getUid());

        constraintLayout = findViewById(R.id.fashtion_layout);

        vintige = findViewById(R.id.vintige); //빈티지
        caejual = findViewById(R.id.caejual); //캐주얼
        dendi = findViewById(R.id.dendi); //댄디
        clrecik = findViewById(R.id.clrecik); //클래식
        abanggard = findViewById(R.id.abanggard); //아방가르드
        amecagi = findViewById(R.id.amecagi); //아메카지
        strit = findViewById(R.id.strit); //스트릿
        spoti = findViewById(R.id.spoti); //스포티
        pungk = findViewById(R.id.pungk); //펑크
        modan = findViewById(R.id.modan); //모던
        minimal = findViewById(R.id.minimal); //미니멀
        militari = findViewById(R.id.militari); //밀리터리
        leiad = findViewById(R.id.leiad); //레이어드
        letro = findViewById(R.id.letro); //레트로
        feminin = findViewById(R.id.feminin); //페미닌
        denim = findViewById(R.id.denim); //데님

        vintige.setOnClickListener(onClickListener);
        caejual.setOnClickListener(onClickListener);
        dendi.setOnClickListener(onClickListener);
        clrecik.setOnClickListener(onClickListener);
        abanggard.setOnClickListener(onClickListener);
        amecagi.setOnClickListener(onClickListener);
        strit.setOnClickListener(onClickListener);
        spoti.setOnClickListener(onClickListener);
        pungk.setOnClickListener(onClickListener);
        modan.setOnClickListener(onClickListener);
        minimal.setOnClickListener(onClickListener);
        militari.setOnClickListener(onClickListener);
        leiad.setOnClickListener(onClickListener);
        letro.setOnClickListener(onClickListener);
        feminin.setOnClickListener(onClickListener);
        denim.setOnClickListener(onClickListener);

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
                    constraintLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.vintige:
                    favorite = vintige.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.caejual:
                    favorite = caejual.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.dendi:
                    favorite = dendi.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.clrecik:
                    favorite = clrecik.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.abanggard:
                    favorite = abanggard.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.amecagi:
                    favorite = amecagi.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.strit:
                    favorite = strit.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.spoti:
                    favorite = spoti.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.pungk:
                    favorite = pungk.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.modan:
                    favorite = modan.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.minimal:
                    favorite = minimal.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.militari:
                    favorite = militari.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.leiad:
                    favorite = leiad.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.letro:
                    favorite = letro.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.feminin:
                    favorite = feminin.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.denim:
                    favorite = denim.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
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
