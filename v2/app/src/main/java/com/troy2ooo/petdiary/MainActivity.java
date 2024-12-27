package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

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

        ImageButton myPetButton = findViewById(R.id.imageButton7);
        ImageButton communityButton = findViewById(R.id.imageButton8);
        ImageButton profileButton = findViewById(R.id.imageButton9);


        // 預設顯示第一個 Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PetFragment())
                    .commit();
        }
        // 設置按鈕的點擊事件來切換 Fragment
        myPetButton.setOnClickListener(v -> switchFragment(new PetFragment()));
        communityButton.setOnClickListener(v -> switchFragment(new CommunityFragment()));
        profileButton.setOnClickListener(v -> switchFragment(new ProfileFragment()));

/*
        // 設置導航選擇事件
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // 根據選項顯示不同 Fragment
            switch (item.getItemId()) {
                case R.id.nav_pet:
                    selectedFragment = new PetFragment();
                    break;
                case R.id.nav_community:
                    selectedFragment = new CommunityFragment();
                    break;
                case R.id.nav_profile:
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
 */


    }
    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
