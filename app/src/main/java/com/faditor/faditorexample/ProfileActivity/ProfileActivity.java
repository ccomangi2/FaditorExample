package com.faditor.faditorexample.ProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SettingActivity.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;

public class ProfileActivity extends Fragment {
    private View view;

    ImageButton setting;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String uid;
    private String currentUserUid;
    private View fragmentView;
//    private FcmPush fcmPush;
    private ListenerRegistration followListenerRegistration;
    private ListenerRegistration followingListenerRegistration;
    private ListenerRegistration imageprofileListenerRegistration;
    private ListenerRegistration recyclerListenerRegistration;
    private HashMap _$_findViewCache;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile, container, false);


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