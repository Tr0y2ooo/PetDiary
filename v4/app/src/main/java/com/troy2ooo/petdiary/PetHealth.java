package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PetHealth extends Fragment {

    private TextView back;  // 定義你的 TextView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將 pet_health.xml 佈局檔案膺上來
        View view = inflater.inflate(R.layout.pet_health, container, false);


        back = view.findViewById(R.id.textView11);

        // 設置 TextView 點擊事件，回到 PetFragment
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 創建 PetFragment 的實例
                PetFragment petFragment = new PetFragment();

                // 使用 FragmentTransaction 來進行切換
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, petFragment)  // 使用原本的容器ID
                        .addToBackStack(null)  // 如果需要返回的話，可以加入回退堆疊
                        .commit();
            }
        });

        //把寵物名字傳過來12
        TextView petName = view.findViewById(R.id.textView3); // 假設有一個 TextView 設定了 ID healthRecordTextView
        // 獲取傳遞過來的資料
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            petName.setText(name);  // 將傳遞的名稱顯示在 TextView 上
        }



        return view;
    }
}
