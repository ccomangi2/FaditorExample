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

public class MyProfileActivity extends Fragment {
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
        view = inflater.inflate(R.layout.activity_myprofile, container, false);


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
//    public final class UserFragmentRecyclerViewAdapter extends RecyclerView.Adapter {
//        private final ArrayList contentDTOs;
//        public final ArrayList getContentDTOs() {
//            return this.contentDTOs;
//        }
//        public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
//
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        }
//
//        public int getItemCount() {
//            return this.contentDTOs.size();
//        }
//
//        public UserFragmentRecyclerViewAdapter(ArrayList contentDTOs) {
//
//            this.contentDTOs = contentDTOs;
//        }
//
//        @Metadata(
//                mv = {1, 1, 18},
//                bv = {1, 0, 3},
//                k = 1,
//                d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t"},
//                d2 = {"Lcom/company/howl/howlstagram/navigation/UserFragment$UserFragmentRecyclerViewAdapter$CustomViewHolder;", "Landroid/support/v7/widget/RecyclerView$ViewHolder;", "imageView", "Landroid/widget/ImageView;", "(Lcom/company/howl/howlstagram/navigation/UserFragment$UserFragmentRecyclerViewAdapter;Landroid/widget/ImageView;)V", "getImageView", "()Landroid/widget/ImageView;", "setImageView", "(Landroid/widget/ImageView;)V", "app"}
//        )
//        public final class CustomViewHolder extends RecyclerView.ViewHolder {
//            @NotNull
//            private ImageView imageView;
//
//            @NotNull
//            public final ImageView getImageView() {
//                return this.imageView;
//            }
//
//            public final void setImageView(@NotNull ImageView var1) {
//
//            }
//
//            public CustomViewHolder(@NotNull ImageView imageView) {
//                super();
//
//            }
//        }
//    }
}