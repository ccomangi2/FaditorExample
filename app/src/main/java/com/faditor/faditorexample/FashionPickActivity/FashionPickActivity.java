package com.faditor.faditorexample.FashionPickActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.faditor.faditorexample.R;
import com.google.firebase.auth.FirebaseAuth;

public class FashionPickActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashionpick);
    }
}