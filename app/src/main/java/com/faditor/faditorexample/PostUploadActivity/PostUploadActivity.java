package com.faditor.faditorexample.PostUploadActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.PostData;
import com.faditor.faditorexample.PostActivity.PostActivity;
import com.faditor.faditorexample.ProfileEditActivity.MediaScanner;
import com.faditor.faditorexample.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_PICTURES;

public class PostUploadActivity extends AppCompatActivity {
    public static final String INTENT_PATH = "path";
    public static final String INTENT_MEDIA = "media";

    public static final int GALLERY_IMAGE = 0;
    public static final int GALLERY_VIDEO = 1;

    private FirebaseAuth mAuth;
    private PostData postInfo;
    EditText text_upload;
    ImageView photo_upload; //게시글 사진
    private ArrayList<String> pathList = new ArrayList<>();

    private int pathCount, successCount;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이것이 필요하다(미디어 스캐닝)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        mAuth = FirebaseAuth.getInstance();

        photo_upload = findViewById(R.id.photo_upload); //게시글 사진
        text_upload = findViewById(R.id.text_upload); //게시글 내용

        findViewById(R.id.back).setOnClickListener(onClickListener); //뒤로가기
        findViewById(R.id.upload).setOnClickListener(onClickListener);
        findViewById(R.id.upload_btn).setOnClickListener(onClickListener);
        findViewById(R.id.camera_btn).setOnClickListener(onClickListener);

        postInfo = (PostData) getIntent().getSerializableExtra("postInfo");
        postInit();

        // 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.
        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        //storage
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference gsReference = storage.getReferenceFromUrl("gs://faditorexmaple.appspot.com/").child("users").child(user.getUid()).child("profile.jpg");
//        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                CircleImageView imageView = findViewById(R.id.profile_image);
//                //이미지 로드 성공시
//                Glide.with(PostUploadActivity.this)
//                        .load(uri)
//                        .into(imageView);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                //이미지 로드 실패시
//                //Toast.makeText(ProfileEditActivity.this, "실패", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upload:
                    storageUpload();
                    break;
                case R.id.upload_btn:
                    getAlbum();
                    break;
                case R.id.camera_btn:
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
    private void storageUpload() {
        final String userId = mAuth.getUid();
        final String content = text_upload.getText().toString();
        final ArrayList<String> contentsList = new ArrayList<>();
        final ArrayList<String> formatList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.collection("Faditor").document("info").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        final String userprofileimage = (String) document.getData().get("photoUri");
                        final String username = (String) document.getData().get("user_name");
                        //final int photo = Integer.parseInt(photoUri.getAuthority().toString());
                        if(photoUri != null) {
                            final String photoName = photoUri.getLastPathSegment();
                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            final DocumentReference documentReference = postInfo == null ? firebaseFirestore.collection("Posts").document() : firebaseFirestore.collection("Posts").document(postInfo.getUserId());
                            final Date date = postInfo == null ? new Date() : postInfo.getPostDate();
                            if(content.length() > 0) {
                                contentsList.add(content);
                                formatList.add(content);
                            }
                            if(pathList.size()!=0) {
                                if (!isStorageUrl(pathList.get(pathCount))) {
                                    String path = pathList.get(pathCount);
                                    successCount++;
                                    contentsList.add(path);
//                                    if (isImageFile(path)) {
//                                        formatList.add("image");
//                                    } else {
//                                        formatList.add("text");
//                                    }
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference();
                                    final StorageReference mountainImagesRef = storageRef.child("post/" + documentReference.getId() + "/" + photoName + ".jpg");
                                    try {
                                        InputStream stream = new FileInputStream(new File("post/" + documentReference.getId() + "/" + photoName + ".jpg"));
                                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                                        UploadTask uploadTask = mountainImagesRef.putStream(stream);
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        successCount--;
                                                        contentsList.set(index, uri.toString());
                                                        if (successCount == 0) {
                                                            PostData postInfo = new PostData(userId, date, contentsList, formatList, photoName, userprofileimage, username);
                                                            storeUpload(documentReference, postInfo);
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } catch (FileNotFoundException e) {
                                        Log.e("로그", "에러: " + e.toString());
                                    }
                                    pathCount++;

                                    Log.v("알림", "현재로그인한 유저 " + userId);
                                }
                            }
                            //final String date_tv = time;
                            if (successCount == 0) {
                                PostData postInfo = new PostData(userId, date, contentsList, formatList, photoName, userprofileimage, username);
                                storeUpload(documentReference, postInfo);
                                gotomain(PostActivity.class);
                            } else {
                                toastMessage("다시 시도해 주세요.");
                            }
                        } else {
                            toastMessage("이미지를 추가해주세요.");
                        }
                    }
                }
            }
        });
    }
    private void storeUpload(DocumentReference documentReference, final PostData postInfo) {
        documentReference.set(postInfo.getPostData())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("postInfo", postInfo);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }
    // 촬영 혹은 크롭된 사진에 대한 새로운 이미지 저장 함수
    private File createImageFile() throws IOException{
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
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.photo_upload);
            String path = data.getStringExtra(INTENT_PATH);
            pathList.set(0, path);
            Glide.with(this).load(path).override(1000).into(imageView);
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
            Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
        }
    };

    // 앨범 불러오기 함수
    private void getAlbum(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
    }

    private void postInit() {
        if (postInfo != null) {
            ArrayList<String> contentsList = postInfo.getContents();
            for (int i = 0; i < contentsList.size(); i++) {
                String contents = contentsList.get(i);
                if (isStorageUrl(contents)) {
                    pathList.add(contents);
                }
            }
        }
    }
    private void gotomain(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public static boolean isStorageUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches() && url.contains("gs://faditorexmaple.appspot.com/");
    }

    public static String storageUrlToName(String url){
        return url.split("\\?")[0].split("%2F")[url.split("\\?")[0].split("%2F").length - 1];
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
}