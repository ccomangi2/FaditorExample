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
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SettingActivity.SettingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyProfileActivity extends Fragment {
    private FirebaseAuth mAuth;
    DatabaseReference usereRef;
    private FirebaseDatabase database;
    private View view;
    ImageButton setting;
    //faditor 정보
    TextView user_name;
    TextView user_intro;
    TextView like_fashion;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myprofile, container, false);

        //faditor 정보
        user_name = view.findViewById(R.id.user_name);
        user_intro = view.findViewById(R.id.info);
        like_fashion = view.findViewById(R.id.like_fashion);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        usereRef = database.getReference("user_list/" + user.getUid());
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://faditorexmaple.appspot.com");

        //생성된 FirebaseStorage를 참조하는 storage 생성
        final StorageReference storageRef = storage.getReference();
        usereRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserFaditorData userFaditorData = dataSnapshot.getValue(UserFaditorData.class);
                //데이터를 화면에 출력해 준다.
                String name = userFaditorData.getUser_name();
                String intro = userFaditorData.getUser_intro();
                String like = userFaditorData.getLike_fashion();
                String profile = userFaditorData.getPhotoname();
                user_name.setText(name);
                user_intro.setText(intro);
                like_fashion.setText(like);

                //Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
                if (profile != null) {
                    StorageReference pathReference = storageRef.child("users/" + profile);
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    Log.d("MyProfileActivity", "Value is: " + name);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MyProfileActivity", "Failed to read value.", error.toException());
            }
        });

        // 설정 화면 이동하기 (설정 버튼 이벤트)
        setting = view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}