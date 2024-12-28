package com.troy2ooo.petdiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class PetFragment extends Fragment {

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 確保在這裡初始化 context
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將 fragment_community.xml 佈局檔案膺上來
        View rootView = inflater.inflate(R.layout.pet_fragment, container, false);
        ImageButton health = rootView.findViewById(R.id.imageButton);
        ImageButton behaver = rootView.findViewById(R.id.imageButton2);
        ImageButton remind = rootView.findViewById(R.id.imageButton3);
        Button edit = rootView.findViewById(R.id.button);

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "你點擊了健康紀錄" , Toast.LENGTH_SHORT).show();
            }
        });
        behaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "你點擊了行為訓練" , Toast.LENGTH_SHORT).show();
            }
        });
        remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "你點擊了提醒功能" , Toast.LENGTH_SHORT).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "你點擊了編輯" , Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;

    }
}