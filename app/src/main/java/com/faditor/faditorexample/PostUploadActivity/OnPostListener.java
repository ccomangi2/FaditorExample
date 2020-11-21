package com.faditor.faditorexample.PostUploadActivity;

import com.faditor.faditorexample.Database.PostData;

public interface OnPostListener {
    void onDelete(PostData postData);
    void onModify();
}
