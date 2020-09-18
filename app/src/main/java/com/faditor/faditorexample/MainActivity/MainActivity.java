package com.faditor.faditorexample.MainActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.faditor.faditorexample.HomeActivity.HomeActivity;
import com.faditor.faditorexample.NoticeActivity.NoticeActivity;
import com.faditor.faditorexample.ProfileActivity.ProfileActivity;
import com.faditor.faditorexample.R;
import com.faditor.faditorexample.SearchActivity.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeActivity fragmentHome = new HomeActivity();
    private SearchActivity fragmentSearch = new SearchActivity();
    private NoticeActivity fragmentNotice = new NoticeActivity();
    private ProfileActivity fragmentProfile = new ProfileActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        getHashkey();
    }
        class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch(menuItem.getItemId())
                {
                    case R.id.homeItem:
                        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                        break;
                    case R.id.searchItem:
                        transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();
                        break;
                    case R.id.addItem:

                        break;
                    case R.id.noticeItem:
                        transaction.replace(R.id.frameLayout, fragmentNotice).commitAllowingStateLoss();
                        break;
                    case R.id.profileItem:
                        transaction.replace(R.id.frameLayout, fragmentProfile).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
    }
    private void getHashkey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}
