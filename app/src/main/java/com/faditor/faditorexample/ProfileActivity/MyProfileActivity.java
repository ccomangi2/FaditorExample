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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.PostActivity.PostActivity;
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

import java.util.ArrayList;

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
    private RecyclerView recyclerView;
    private UserPostAdapter adapter;
    private GridLayoutManager layoutManager;
    private ArrayList<PostData> arrayList;

    DatabaseReference postRef;
    private FirebaseDatabase postdatabase;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myprofile, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.grid_recyclerview);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        recyclerView.setLayoutManager(layoutManager);

        layoutManager = new GridLayoutManager(getContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int gridPosition = position % 6;
                switch (gridPosition) {
                    case 0:
                    case 1:
                    case 2:
                        return 2;
                    case 3:
                    case 4:
                    case 5:
                        return 5;
                }
                return 0;
            }
        });

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

        ///
        postdatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        postRef = postdatabase.getReference("user_list/" + user.getUid());
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserFaditorData userFaditorData = dataSnapshot.getValue(UserFaditorData.class);
                //데이터를 화면에 출력해 준다.
                String name = userFaditorData.getUser_name();
                database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                databaseReference = database.getReference("content_list/" + name); // DB 테이블 연결
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                            PostData postData = snapshot.getValue(PostData.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                            arrayList.add(postData); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                        }
                        adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("에러", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MyProfileActivity", "Failed to read value.", error.toException());
            }
        });
        recyclerView.setAdapter(adapter);
        adapter = new UserPostAdapter(arrayList, getContext());

        adapter.setOnItemClicklistener(new OnPostItemClickListener() {
            @Override
            public void onItemClick(UserPostAdapter.CustomViewHolder holder, View view, int position) {
                PostData item = adapter.getItem(position);
                String img_name = item.getPhotoName();
                Intent intent = new Intent(getContext(), PostActivity.class);
                intent.putExtra("postimg", img_name);
                startActivity(intent);
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