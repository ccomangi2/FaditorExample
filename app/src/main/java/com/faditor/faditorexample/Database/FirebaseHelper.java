package com.faditor.faditorexample.Database;

import android.app.Activity;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.faditor.faditorexample.PostUploadActivity.OnPostListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URLConnection;
import java.util.ArrayList;

public class FirebaseHelper {
    private Activity activity;
    private OnPostListener onPostListener;
    private int successCount;

    public FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void setOnPostListener(OnPostListener onPostListener){
        this.onPostListener = onPostListener;
    }

    public void storageDelete(final PostData postInfo){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ArrayList<String> contentsList = postInfo.getContents();
        final String id = postInfo.getUserId();
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrl(contents)) {
                successCount++;
                StorageReference desertRef = storageRef.child("posts/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id, postInfo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast("Error");
                    }
                });
            }
        }
        storeDelete(id, postInfo);
    }

    private void storeDelete(final String id, final PostData postInfo) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0) {
            firebaseFirestore.collection("Posts").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast("게시글을 삭제하였습니다.");
                            onPostListener.onDelete(postInfo);
                            //postsUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("게시글을 삭제하지 못하였습니다.");
                        }
                    });
        }
    }
    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
    public static String storageUrlToName(String url){
        return url.split("\\?")[0].split("%2F")[url.split("\\?")[0].split("%2F").length - 1];
    }
    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }
    public static boolean isStorageUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches() && url.contains("gs://faditorexmaple.appspot.com/post");
    }
}
