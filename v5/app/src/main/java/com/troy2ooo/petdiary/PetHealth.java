package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PetHealth extends Fragment {

    private TextView back;  // 定義返回的 TextView
    private TextView vaccineDateText;
    private MaterialButton selectVaccineDateButton;
    private TextInputEditText medicineNameInput;
    private TextInputEditText medicineDateInput;
    private MaterialButton addMedicineButton;
    private HealthRecordAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將 pet_health.xml 佈局檔案膺上來
        View view = inflater.inflate(R.layout.pet_health, container, false);

        back = view.findViewById(R.id.textView11);
        vaccineDateText = view.findViewById(R.id.vaccineDateText);
        selectVaccineDateButton = view.findViewById(R.id.selectVaccineDateButton);
        medicineNameInput = view.findViewById(R.id.medicineNameInput);
        medicineDateInput = view.findViewById(R.id.medicineDateInput);

        // 初始化 RecyclerView
        RecyclerView healthRecordsList = view.findViewById(R.id.healthRecordsList);
        healthRecordsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HealthRecordAdapter();
        healthRecordsList.setAdapter(adapter);

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

        // 把寵物名字傳過來
        TextView petName = view.findViewById(R.id.textView3); // 假設有一個 TextView 設定了 ID healthRecordTextView
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            petName.setText(name);  // 將傳遞的名稱顯示在 TextView 上
        }

        // 設置疫苗日期選擇
        selectVaccineDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                        vaccineDateText.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // 設置藥物時間選擇
        medicineDateInput.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
                        medicineDateInput.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });



        // 設置添加健康日誌按鈕的點擊事件
        MaterialButton addHealthLogButton = view.findViewById(R.id.addHealthLogButton);
        addHealthLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 獲取所有輸入的健康資訊
                String weight = ((TextView) view.findViewById(R.id.weightEditText)).getText().toString();
                int healthStatus = ((SeekBar) view.findViewById(R.id.healthStatusSeekBar)).getProgress();
                String vaccineDate = ((TextView) view.findViewById(R.id.vaccineDateText)).getText().toString();
                String medicineName = ((TextInputEditText) view.findViewById(R.id.medicineNameInput)).getText().toString();
                String medicineDate = ((TextInputEditText) view.findViewById(R.id.medicineDateInput)).getText().toString();

                // 檢查必要資訊是否填寫
                if (weight.isEmpty()) {
                    Toast.makeText(getActivity(), "請輸入體重", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 創建健康日誌內容
                StringBuilder logContent = new StringBuilder();
                logContent.append("體重: ").append(weight).append("公斤\n");
                
                // 添加健康狀態
                String healthStatusText;
                if (healthStatus < 33) {
                    healthStatusText = "不佳";
                } else if (healthStatus < 66) {
                    healthStatusText = "普通";
                } else {
                    healthStatusText = "良好";
                }
                logContent.append("健康狀態: ").append(healthStatusText).append("\n");

                // 添加疫苗資訊
                if (!vaccineDate.equals("選擇日期")) {
                    logContent.append("最近疫苗接種: ").append(vaccineDate).append("\n");
                }

                // 添加用藥資訊
                if (!medicineName.isEmpty() && !medicineDate.isEmpty()) {
                    logContent.append("用藥紀錄: ").append(medicineName)
                             .append(" (").append(medicineDate).append(")\n");
                }

                // 創建一個 AlertDialog 來預覽和確認
                new AlertDialog.Builder(getContext())
                    .setTitle("健康日誌預覽")
                    .setMessage(logContent.toString())
                    .setPositiveButton("確認添加", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 獲取當前日期
                            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    .format(new Date());
                            
                            // 創建新的健康紀錄
                            HealthRecord newRecord = new HealthRecord(currentDate, logContent.toString());
                            adapter.addRecord(newRecord);
                            
                            Toast.makeText(getActivity(), "健康日誌已添加", Toast.LENGTH_SHORT).show();
                            
                            // 清空輸入
                            ((EditText) view.findViewById(R.id.weightEditText)).setText("");
                            ((SeekBar) view.findViewById(R.id.healthStatusSeekBar)).setProgress(50);
                            ((TextView) view.findViewById(R.id.vaccineDateText)).setText("選擇日期");
                            ((TextInputEditText) view.findViewById(R.id.medicineNameInput)).setText("");
                            ((TextInputEditText) view.findViewById(R.id.medicineDateInput)).setText("");
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            }
        });

        return view;
    }
}

