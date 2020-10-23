package com.faditor.faditorexample.PostUploadActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostUploadActivity extends AppCompatActivity {

    private static final String TAG = "PostUploadActivity";
    private FirebaseUser user;

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        // 뒤로 가기
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.upload).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.upload:
                    PostUpload();
                    break;
            }
        }
    };

    //postUpload
    private void PostUpload(){
        final String contents = ((EditText)findViewById(R.id.text_upload)).getText().toString();

        if(contents.length() > 0){
           user = FirebaseAuth.getInstance().getCurrentUser();
            WriteInfo writeInfo = new WriteInfo(contents, user.getUid());
            uploader(writeInfo);
        }else{
            startToast("내용을 입력해주세요");
        }
    }

    // upload
    // 근데 upload가 안돼요;;ㅠㅠ
    private void uploader(WriteInfo writeInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        {
                            startToast("");
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}