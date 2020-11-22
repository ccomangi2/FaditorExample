package com.faditor.faditorexample.ProfileActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfileActivity extends Fragment {
    private FirebaseAuth mAuth;
    private View view;
    ImageButton setting;

    //faditor 정보
    TextView user_name;
    TextView user_intro;
    TextView like_fashion;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myprofile, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(user.getUid());

        // 설정 화면 이동하기 (설정 버튼 이벤트)
        setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        //faditor 정보
        user_name = view.findViewById(R.id.user_name);
        user_intro = view.findViewById(R.id.info);
        like_fashion = view.findViewById(R.id.like_fashion);
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

        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl("gs://faditorexmaple.appspot.com/").child("users").child(user.getUid()).child("profile.jpg");
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView imageView = view.findViewById(R.id.account_iv_profile);
                //이미지 로드 성공시
                Glide.with(MyProfileActivity.this)
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                //Toast.makeText(ProfileEditActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}