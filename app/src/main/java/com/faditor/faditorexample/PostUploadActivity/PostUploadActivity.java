package com.faditor.faditorexample.PostUploadActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.PostActivity.PostActivity;
import com.faditor.faditorexample.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class PostUploadActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PostData postInfo;

    EditText text_upload;
    ImageView photo_upload; //게시글 사진

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        photo_upload = findViewById(R.id.photo_upload); //게시글 사진
        text_upload = findViewById(R.id.text_upload); //게시글 내용

        findViewById(R.id.upload).setOnClickListener(onClickListener);

        postInfo = (PostData) getIntent().getSerializableExtra("postInfo");
        postInit();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upload:
                    storageUpload();
                    break;
            }
        }
    };
    private void storageUpload() {
        String userId = mAuth.getUid();
        String content = text_upload.getText().toString();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = postInfo == null ? firebaseFirestore.collection("Posts").document() : firebaseFirestore.collection("Posts").document(postInfo.getUserId());
        final Date date = postInfo == null ? new Date() : postInfo.getPostDate();

        Log.v("알림", "현재로그인한 유저 " + userId);

        //final String date_tv = time;
        if(photo_upload != null) {
            PostData postData = new PostData(userId, date, content, "");
            storeUpload(documentReference, postData);
            gotomain(PostActivity.class);
        } else {
            toastMessage("이미지를 추가해주세요.");
        }
    }
    private void storeUpload(DocumentReference documentReference, final PostData postInfo) {
        documentReference.set(postInfo.getPostData())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postInfo", postInfo);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }
    private void postInit() {
        if (postInfo != null) {
            text_upload.setText(postInfo.getContents());
        }
    }
    private void gotomain(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}