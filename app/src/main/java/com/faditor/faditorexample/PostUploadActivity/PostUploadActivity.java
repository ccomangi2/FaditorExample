package com.faditor.faditorexample.PostUploadActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import com.faditor.faditorexample.Database.NoticeData;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.MainActivity.MainActivity;
import com.faditor.faditorexample.ProfileEditActivity.MediaScanner;
import com.faditor.faditorexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_PICTURES;

public class PostUploadActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    private DatabaseReference mNoticeReference;
    private FirebaseAuth mAuth;
    EditText text_upload;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;
    DatabaseReference usereRef;
    private FirebaseDatabase database;

    int position;

    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이것이 필요하다(미디어 스캐닝)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        text_upload = findViewById(R.id.text_upload);
        // 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.
        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        findViewById(R.id.back).setOnClickListener(onClickListener);
        findViewById(R.id.upload).setOnClickListener(onClickListener);
        findViewById(R.id.upload_btn).setOnClickListener(onClickListener);
        findViewById(R.id.camera_btn).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //뒤로가기
                    gotomain(MainActivity.class);
                    break;
                case R.id.upload: //수정완료
                    if(photoUri != null) {
                        postFirebaseDatabase(true);
                    } else {
                        toastMessage("이미지를 추가해주세요.");
                    }
                    break;
                case R.id.upload_btn: //라이브러리
                    getAlbum();
                    break;
                case R.id.camera_btn: //카메라 촬영
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {

                        }
                        if (photoFile != null) {
                            photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                    break;
            }
        }
    };

    public void postFirebaseDatabase(final boolean add) {
        final RelativeLayout loderLayout = findViewById(R.id.loaderLayout);
        loderLayout.setVisibility(View.VISIBLE);
        mPostReference = FirebaseDatabase.getInstance().getReference();
        mNoticeReference = FirebaseDatabase.getInstance().getReference();
        final Map<String, Object> childUpdates = new HashMap<>();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        usereRef = database.getReference("user_list/" + user.getUid());
        FirebaseStorage storageuser = FirebaseStorage.getInstance("gs://faditorexmaple.appspot.com");

        //생성된 FirebaseStorage를 참조하는 storage 생성
        final StorageReference storageRef = storageuser.getReference();
        usereRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserFaditorData userFaditorData = dataSnapshot.getValue(UserFaditorData.class);
                //데이터를 화면에 출력해 준다.
                final String username = userFaditorData.getUser_name();
                String intro = userFaditorData.getUser_intro();
                String like = userFaditorData.getLike_fashion();
                final String userprofileimage = userFaditorData.getPhotoUri();
                final String userprofilename = userFaditorData.getPhotoname();

                Date mDate = new Date();
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                final String postdate = simpleDate.format(mDate);
                final String contents = text_upload.getText().toString();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://faditorexmaple.appspot.com").child("posts/" + photoUri.getLastPathSegment());
                storageRef.putFile(photoUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    loderLayout.setVisibility(View.GONE);
                                    final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                                    while (!imageUrl.isComplete()) ;
                                    Map<String, Object> postValues = null;
                                    if (add) {
                                        PostData post = new PostData(userprofileimage, username, postdate, contents, photoUri.getLastPathSegment(), imageUrl.getResult().toString(), userprofilename);
                                        postValues = post.getPostData();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                        FirebaseDatabase noticedatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
                                        DatabaseReference noticeRef = noticedatabase.getReference("user_list/" + user.getUid());
                                        noticeRef.addValueEventListener(new ValueEventListener() {
                                            Map<String, Object> noticeValues = null;
                                            @Override
                                            public void onDataChange(DataSnapshot data) {
                                                UserFaditorData userData = data.getValue(UserFaditorData.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                                                String name = userData.getUser_name();

                                                NoticeData noticeData = new NoticeData(name);
                                                noticeValues =noticeData.getNoticeData();

                                                mNoticeReference.child("/notice_list/").push().setValue(noticeValues);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                // Failed to read value
                                                Log.w("MyProfileActivity", "Failed to read value.", error.toException());
                                            }
                                        });
                                    }
                                    //childUpdates.put("/content_list/" + username, postValues);
                                    mPostReference.child("/content_list/").child(username).child(Objects.requireNonNull(photoUri.getLastPathSegment())).setValue(postValues);
                                }
                            }
                        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("에러", String.valueOf(error.toException())); // 에러문 출력
            }
        });
    }
            public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }
    // 촬영 혹은 크롭된 사진에 대한 새로운 이미지 저장 함수
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);
            } else {
                exifDegree = 0;
            }

            String result = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());
            String filename = formatter.format(curDate);

            String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "Faditor" + File.separator;
            File file = new File(strFolderName);
            if (!file.exists())
                file.mkdirs();

            File f = new File(strFolderName + "/" + filename + ".png");
            result = f.getPath();

            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = "Save Error fOut";
            }

            // 비트맵 사진 폴더 경로에 저장
            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);

            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
                // 방금 저장된 사진을 갤러리 폴더 반영 및 최신화
                mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".png");
            } catch (IOException e) {
                e.printStackTrace();
                result = "File close Error";
            }

            // 이미지 뷰에 비트맵을 set하여 이미지 표현
            ((ImageView) findViewById(R.id.photo_upload)).setImageBitmap(rotate(bitmap, exifDegree));
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            photoUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                ((ImageView) findViewById(R.id.photo_upload)).setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            //이미지 뷰에 비트맵을 set하여 이미지 표현
        }
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // 권한 확인
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };

    // 앨범 불러오기 함수
    private void getAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }
    private void gotomain(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
