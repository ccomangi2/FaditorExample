package com.faditor.faditorexample.PostActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.PostUploadActivity.PostUploadActivity;
import com.faditor.faditorexample.R;
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

public class PostActivity extends AppCompatActivity {
    private PostData postInfo;
    private LinearLayout contentsLayout;
    private FirebaseAuth mAuth;
    DatabaseReference usereRef;
    private FirebaseDatabase userdatabase;
    private ArrayList<PostData> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    ImageView account_iv_profile;
    ImageView photo_upload;
    TextView user_name;
    TextView post_date;
    TextView text_upload;
    TextView like_people;
    CheckBox content_heart_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        uiUpdate();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 0:
//                if (resultCode == Activity.RESULT_OK) {
//                    uiUpdate();
//                }
//                break;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                return true;
            case R.id.modify:
                myStartActivity(PostUploadActivity.class, postInfo);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uiUpdate(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userdatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        usereRef = userdatabase.getReference("user_list/");
        usereRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserFaditorData userFaditorData = dataSnapshot.getValue(UserFaditorData.class);
                //데이터를 화면에 출력해 준다.
                String name = userFaditorData.getUser_name();
                String getin = getIntent().getStringExtra("postimg");
                database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                databaseReference = database.getReference("content_list/" + name + "/" + getin); // DB 테이블 연결
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PostData item = dataSnapshot.getValue(PostData.class);
                        account_iv_profile = findViewById(R.id.account_iv_profile);
                        photo_upload = findViewById(R.id.photo_upload);
                        user_name = findViewById(R.id.user_name);
                        post_date = findViewById(R.id.post_date);
                        text_upload = findViewById(R.id.text_upload);
                        like_people = findViewById(R.id.like_people);
                        content_heart_button = findViewById(R.id.content_heart_button);

                        final String profile = item.getUserprofileName();
                        final String photo = item.getPhotoName();
                        String name = item.getUsername();
                        String date = item.getPostDate();
                        String text = item.getContents();
                        String like = String.valueOf(item.getStarCount());

                        user_name.setText(name);
                        post_date.setText(date);
                        text_upload.setText(text);
                        like_people.setText(like);

                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://faditorexmaple.appspot.com");
                        StorageReference storageRef = storage.getReference();
                        StorageReference pathReference = storageRef.child("users/" + profile);
                        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //이미지 로드 성공시
                                Glide.with(PostActivity.this)
                                        .load(uri)
                                        .into(account_iv_profile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("에러", String.valueOf(e)); // 에러문 출력
                            }
                        });

                        StorageReference postReference = storageRef.child("posts/" + photo);
                        postReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //이미지 로드 성공시
                                Glide.with(PostActivity.this)
                                        .load(uri)
                                        .into(photo_upload);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("에러2", String.valueOf(e)); // 에러문 출력
                            }
                        });
                        Log.d("MyProfileActivity", "Value is: " + name);
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
    }


    private void myStartActivity(Class c, PostData postInfo) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}
