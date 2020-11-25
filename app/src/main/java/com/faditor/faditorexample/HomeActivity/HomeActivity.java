package com.faditor.faditorexample.HomeActivity;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faditor.faditorexample.Adapter.PostAdapter;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.PaperActivity.PaperActivity;
import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SettingActivity.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends Fragment {
    private View view;
    // 상단바 메뉴 버튼
    ImageButton button_news; //뉴스
    ImageButton button_setting; //설정
    private FirebaseAuth mAuth;
    DatabaseReference usereRef;
    DatabaseReference faditorRef;
    private FirebaseDatabase userdatabase;
    private RecyclerView recyclerView;
    private RecyclerView faditor_recyclerView;
    private PostAdapter adapter;
    private FaditorAdapter useradapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private ArrayList<PostData> arrayList;
    private ArrayList<UserFaditorData> userList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    //best look
    ImageView bestlook1, bestlook2;
    TextView bestlookuser1, bestlookuser2;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, null);

        recyclerView = view.findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        bestlook1 = view.findViewById(R.id.best_look01);
        bestlook2 = view.findViewById(R.id.best_look02);
        bestlookuser1 = view.findViewById(R.id.best_look_user1);
        bestlookuser2 = view.findViewById(R.id.best_look_user2);

        faditor_recyclerView = view.findViewById(R.id.recyclerView_faditor); // 아디 연결
        faditor_recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        faditor_recyclerView.setLayoutManager(layoutManager2);
        userList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userdatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        usereRef = userdatabase.getReference("user_list/" + user.getUid());
        usereRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserFaditorData userFaditorData = data.getValue(UserFaditorData.class);
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

        userdatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        faditorRef = userdatabase.getReference("user_list/");
        faditorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                userList.clear();
                for (DataSnapshot snapshot : data.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    UserFaditorData postData = snapshot.getValue(UserFaditorData.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    userList.add(postData); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                useradapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("MyProfileActivity", "Failed to read value.", error.toException());
            }
        });

        adapter = new PostAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        useradapter = new FaditorAdapter(userList, getContext());
        faditor_recyclerView.setAdapter(useradapter);

        button_news = view.findViewById(R.id.news_button);
        button_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaperActivity.class);
                startActivity(intent);
            }
        });
        button_setting = view.findViewById(R.id.setting_button);
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        //best look



        return view;
    }
}
