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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.RatingBar;

public class PetBehaver extends Fragment {

    private TextView back;  // 定義你的 TextView
    private Button addTrain ;
    private RecyclerView recyclerView;
    private BehaviorAdapter adapter;
    private List<String> behaviors = new ArrayList<>();
    private LinearLayout trainingItemsContainer;  // 用來存放訓練項目的容器


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將 pet_health.xml 佈局檔案膺上來
        View view = inflater.inflate(R.layout.pet_behaver, container, false);

        addTrain = view.findViewById(R.id.addTrainingButton);
        back = view.findViewById(R.id.textView11);

        /*recyclerView = view.findViewById(R.id.trainingRecordsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // 設置布局管理器
        adapter = new BehaviorAdapter(behaviors);  // 初始化適配器
        recyclerView.setAdapter(adapter);*/

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

        // 設置新增訓練項目按鈕
        MaterialButton addTrainingButton = view.findViewById(R.id.addTrainingButton);
        addTrainingButton.setOnClickListener(v -> showAddTrainingDialog());

        //把寵物名字傳過來12
        TextView petName = view.findViewById(R.id.textView3); // 假設有一個 TextView 設定了 ID healthRecordTextView
        // 獲取傳遞過來的資料
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            petName.setText(name);  // 將傳遞的名稱顯示在 TextView 上
        }

        // 設置預設項目的評分監聽
        RatingBar handshakeRating = view.findViewById(R.id.handshakeRating);
        RatingBar toiletRating = view.findViewById(R.id.toiletRating);
        RatingBar recallRating = view.findViewById(R.id.recallRating);

        setupRatingBar(handshakeRating, "握手");
        setupRatingBar(toiletRating, "不亂尿尿");
        setupRatingBar(recallRating, "一鍵回城");

        return view;
    }

    private void showAddTrainingDialog() {
        // 創建對話框的 View
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_training, null);
        TextInputEditText trainingNameInput = dialogView.findViewById(R.id.trainingNameInput);

        // 創建並顯示對話框
        new AlertDialog.Builder(getContext())
            .setTitle("新增訓練項目")
            .setView(dialogView)
            .setPositiveButton("確定", (dialog, which) -> {
                String trainingName = trainingNameInput.getText().toString();
                if (!trainingName.isEmpty()) {
                    addNewTrainingItem(trainingName);
                }
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void addNewTrainingItem(String trainingName) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.training_item, null);
        
        TextView nameText = itemView.findViewById(R.id.trainingName);
        nameText.setText(trainingName);
        
        RatingBar ratingBar = itemView.findViewById(R.id.trainingRating);
        setupRatingBar(ratingBar, trainingName);  // 為新增的項目也添加評分監聽
        
        LinearLayout container = getView().findViewById(R.id.trainingItemsContainer);
        if (container != null) {
            container.addView(itemView, container.getChildCount());
            Toast.makeText(getContext(), "已新增訓練項目：" + trainingName, Toast.LENGTH_SHORT).show();
        }
    }

    // 添加設置 RatingBar 的方法
    private void setupRatingBar(RatingBar ratingBar, String itemName) {
        ratingBar.setOnRatingBarChangeListener((rBar, rating, fromUser) -> {
            if (fromUser) {
                Toast.makeText(getContext(), 
                    itemName + " 訓練程度：" + (int)rating + "星", 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }
}
