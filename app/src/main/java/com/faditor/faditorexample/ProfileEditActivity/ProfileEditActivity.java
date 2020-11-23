package com.faditor.faditorexample.ProfileEditActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.faditor.faditorexample.Database.UserData;
import com.faditor.faditorexample.Database.UserFaditorData;
import com.faditor.faditorexample.MainActivity.MainActivity;
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
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.DIRECTORY_PICTURES;

public class ProfileEditActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int pathCount, successCount;

    CardView lila;

    private static final int REQUEST_TAKE_PHOTO = 1111;
    private static final int REQUEST_TAKE_ALBUM = 2222;
    private static final int REQUEST_TAKE_CROP = 3333;

    String mCurrentPhotoPath;
    Uri imageURI;

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이것이 필요하다(미디어 스캐닝)

    //개인정보 edittext
    EditText b_name, b_email;

    //faditor 정보
    EditText user_name;
    EditText user_intro;
    TextView like_fashion;

    //패션 분야 선택 레이아웃
    ConstraintLayout constraintLayout;

    Button vintige; //빈티지
    Button caejual; //캐주얼
    Button dendi; //댄디
    Button clrecik; //클래식
    Button abanggard; //아방가르드
    Button amecagi; //아메카지
    Button strit; //스트릿
    Button spoti; //스포티
    Button pungk; //펑크
    Button modan; //모던
    Button minimal; //미니멀
    Button militari; //밀리터리
    Button leiad; //레이어드
    Button letro; //레트로
    Button feminin; //페미닌
    Button denim; //데님

    String favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileedit);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DocumentReference docRef = db.collection("Users").document(user.getUid());

        constraintLayout = findViewById(R.id.fashtion_layout); //선택하기 누를 시
        lila = (CardView) findViewById(R.id.select_buttons); //프로필 사진 바꾸기 누를 시

        // 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.
        mMediaScanner = MediaScanner.getInstance(getApplicationContext());

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        vintige = findViewById(R.id.vintige); //빈티지
        caejual = findViewById(R.id.caejual); //캐주얼
        dendi = findViewById(R.id.dendi); //댄디
        clrecik = findViewById(R.id.clrecik); //클래식
        abanggard = findViewById(R.id.abanggard); //아방가르드
        amecagi = findViewById(R.id.amecagi); //아메카지
        strit = findViewById(R.id.strit); //스트릿
        spoti = findViewById(R.id.spoti); //스포티
        pungk = findViewById(R.id.pungk); //펑크
        modan = findViewById(R.id.modan); //모던
        minimal = findViewById(R.id.minimal); //미니멀
        militari = findViewById(R.id.militari); //밀리터리
        leiad = findViewById(R.id.leiad); //레이어드
        letro = findViewById(R.id.letro); //레트로
        feminin = findViewById(R.id.feminin); //페미닌
        denim = findViewById(R.id.denim); //데님

        vintige.setOnClickListener(onClickListener);
        caejual.setOnClickListener(onClickListener);
        dendi.setOnClickListener(onClickListener);
        clrecik.setOnClickListener(onClickListener);
        abanggard.setOnClickListener(onClickListener);
        amecagi.setOnClickListener(onClickListener);
        strit.setOnClickListener(onClickListener);
        spoti.setOnClickListener(onClickListener);
        pungk.setOnClickListener(onClickListener);
        modan.setOnClickListener(onClickListener);
        minimal.setOnClickListener(onClickListener);
        militari.setOnClickListener(onClickListener);
        leiad.setOnClickListener(onClickListener);
        letro.setOnClickListener(onClickListener);
        feminin.setOnClickListener(onClickListener);
        denim.setOnClickListener(onClickListener);

        findViewById(R.id.back).setOnClickListener(onClickListener);
        findViewById(R.id.ok).setOnClickListener(onClickListener);
        findViewById(R.id.fashion_choise_btn).setOnClickListener(onClickListener);

        findViewById(R.id.image_change).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gallery).setOnClickListener(onClickListener);
        findViewById(R.id.btn_camera).setOnClickListener(onClickListener);
        findViewById(R.id.btn_cancel).setOnClickListener(onClickListener);

        //개인정보
        b_name = findViewById(R.id.name_edit);
        b_email = findViewById(R.id.email_edit);
        docRef.collection("Basic").document("info").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        b_name.setText(document.getData().get("name").toString());
                        if(document.getData().get("email") != null) {
                            b_email.setText(document.getData().get("email").toString());
                        }
                        if(document.getData().get("email") == null) {
                            b_email.setHint("email");
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        //faditor 정보
        user_name = findViewById(R.id.username_edit);
        user_intro = findViewById(R.id.info_edit);
        like_fashion = findViewById(R.id.like_fashion);
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
                CircleImageView imageView = findViewById(R.id.profile_image);
                //이미지 로드 성공시
                Glide.with(ProfileEditActivity.this)
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

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back: //뒤로가기
                    gotomain(MainActivity.class);
                    break;
                case R.id.ok: //수정완료
                    storageUpload();
                    break;
                case R.id.image_change: //프로필 이미지 바꾸기
                    lila.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_gallery: //라이브러리
                    getAlbum();
                    lila.setVisibility(View.GONE);
                    break;
                case R.id.btn_camera: //카메라 촬영
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
                            lila.setVisibility(View.GONE);
                        }
                    }
                    break;
                case R.id.btn_cancel: //취소 버튼
                    lila.setVisibility(View.GONE);
                    break;
                case R.id.fashion_choise_btn: //패션 분야 선택
                    constraintLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.vintige:
                    favorite = vintige.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.caejual:
                    favorite = caejual.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.dendi:
                    favorite = dendi.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.clrecik:
                    favorite = clrecik.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.abanggard:
                    favorite = abanggard.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.amecagi:
                    favorite = amecagi.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.strit:
                    favorite = strit.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.spoti:
                    favorite = spoti.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.pungk:
                    favorite = pungk.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.modan:
                    favorite = modan.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.minimal:
                    favorite = minimal.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.militari:
                    favorite = militari.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.leiad:
                    favorite = leiad.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.letro:
                    favorite = letro.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.feminin:
                    favorite = feminin.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
                case R.id.denim:
                    favorite = denim.getText().toString();
                    like_fashion.setText(favorite);
                    constraintLayout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void storageUpload() {
        //패디터 정보
        String userId = mAuth.getUid();
        String name = user_name.getText().toString();
        String intro = user_intro.getText().toString();
        String fashion = like_fashion.getText().toString();
        String photouri = photoUri.getLastPathSegment();

        //개인정보
        String basic_name = b_name.getText().toString();
        String basic_email = b_email.getText().toString();

        Log.v("알림", "현재로그인한 유저 " + userId);
        Log.v("알림", "사용자 이름 " + name);
        Log.v("알림", "한줄 소개 " + intro);
        Log.v("알림", "관심 패션 분야 " + fashion);

        //final String date_tv = time;
        if(user_name.length() > 0 && b_name.length() > 0 && b_email.length() > 0) {
            UserFaditorData userFaditorData = new UserFaditorData(name, intro, fashion, photouri);
            storeUpload(userFaditorData);
            UserData userdata = new UserData(basic_name, basic_email);
            profileUpload(userdata);
            gotomain(SettingActivity.class);
        } else if(user_name.length() == 0 && b_name.length() > 0 && b_email.length() > 0){
            toastMessage("사용자 이름을 입력해 주세요.");
        } else if(user_name.length() > 0 && b_name.length() > 0 && b_email.length() == 0){
            toastMessage("이메일을 입력해 주세요.");
        } else if(user_name.length() > 0 && b_name.length() == 0 && b_email.length() > 0){
            toastMessage("이름을 입력해 주세요.");
        } else {
            toastMessage("입력란을 확인해 주세요.");
        }
    }
    private void storeUpload(UserFaditorData userFaditorData) {
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = userFaditorData == null ? firebaseFirestore.collection("users").document() : firebaseFirestore.collection("users").document(user.getUid());
        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child("users/" + documentReference.getId() + "/" + "profile.jpg");
        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/profile.jpg");

        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        ImageView imageView = findViewById(R.id.profile_image);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File("users/" + documentReference.getId() + "/" + "profile.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(stream != null) {
            uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }

        db.collection("Users").document(user.getUid()).collection("Faditor").document("info").set(userFaditorData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("알림", "DocumentSnapshot added with ID: " + d.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("알림", "Error adding document", e);
                    }
                });
    }
    private void profileUpload(UserData userdata) {
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(user.getUid()).collection("Basic").document("info").set(userdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d("알림", "DocumentSnapshot added with ID: " + d.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("알림", "Error adding document", e);
                    }
                });
    }

    private void gotomain(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
    private void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
            ((ImageView) findViewById(R.id.profile_image)).setImageBitmap(rotate(bitmap, exifDegree));
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            photoUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                ((ImageView) findViewById(R.id.profile_image)).setImageBitmap(bitmap);
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
}
