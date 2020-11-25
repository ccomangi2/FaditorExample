package com.faditor.faditorexample.SearchActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.ProfileActivity.ProfileActivity;
import com.faditor.faditorexample.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends Fragment {
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserFaditorData> arrayList;
    private ArrayList<UserFaditorData> saveList;//회원검색 기능용 복사본
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    public View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerView); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)
        saveList = new ArrayList<>();

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("user_list"); // DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    UserFaditorData user = snapshot.getValue(UserFaditorData.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                    arrayList.add(user); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    saveList.add(user);
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        adapter = new SearchAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        adapter.setOnItemClicklistener(new OnSearchItemClickListener() {
            @Override
            public void onItemClick(SearchAdapter.CustomViewHolder holder, View view, int position) {
                UserFaditorData item = adapter.getItem(position);
                String userId = item.getUser_id();
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        EditText search = (EditText)view.findViewById(R.id.searchBar);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString());//회원 검색 기능용

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
    public void searchUser(String search){
        arrayList.clear();
        for(int i = 0; i < saveList.size(); i++){
            if(saveList.get(i).getUser_name().contains(search)){//contains메소드로 search 값이 있으면 true를 반환함
                arrayList.add(saveList.get(i));
            }
        }
        adapter.notifyDataSetChanged();//어댑터에 값일 바뀐것을 알려줌
    }
}
