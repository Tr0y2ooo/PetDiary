package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.view.View;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;



import androidx.fragment.app.Fragment;

public class PetBehaver extends Fragment {

    private TextView back;  // 定義你的 TextView
    private Button addTrain ;
    private RecyclerView recyclerView;
    private BehaviorAdapter adapter;
    private List<String> behaviors = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將 pet_health.xml 佈局檔案膺上來
        View view = inflater.inflate(R.layout.pet_behaver, container, false);

        addTrain = view.findViewById(R.id.addTrainButton);
        back = view.findViewById(R.id.textView11);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // 設置布局管理器
        adapter = new BehaviorAdapter(behaviors);  // 初始化適配器
        recyclerView.setAdapter(adapter);

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

        // 設置按鈕點擊事件
        addTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 彈出 AlertDialog 讓用戶輸入訓練行為
                showAddTrainDialog();
            }
        });

        return view;
    }
    private void showAddTrainDialog() {
        // 顯示 AlertDialog 讓用戶輸入新行為
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText input = new EditText(getContext());
        builder.setTitle("新增行為")
                .setMessage("請輸入行為名稱")
                .setView(input)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String behavior = input.getText().toString();
                        behaviors.add(behavior);  // 添加新行為到列表
                        adapter.notifyItemInserted(behaviors.size() - 1);  // 通知適配器有新項目
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


}
