package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.os.Debug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 預設顯示第一個 Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PetFragment())
                    .commit();
        }



        // 設置導航選擇事件
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;


            // 根據選項顯示不同 Fragment

            switch (item.getItemId()) {

                case R.id.nav_my_pet:
                    selectedFragment = new PetFragment();
                    break;
                case R.id.nav_community:
                    selectedFragment = new CommunityFragment();
                    break;
                case R.id.nav_user_profile:
                    selectedFragment = new ProfileFragment();
                    break;

            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
