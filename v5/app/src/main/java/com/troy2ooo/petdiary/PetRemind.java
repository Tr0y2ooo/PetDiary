package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;
import android.app.DatePickerDialog;

import android.widget.Toast;
import java.text.SimpleDateFormat;


import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import android.widget.LinearLayout;

import java.util.Locale;


// 添加缺少的 imports
import android.app.TimePickerDialog;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.Fragment;




import androidx.fragment.app.Fragment;

public class PetRemind extends Fragment {
    private TextView back;
    private MaterialButton addRemindButton;
    private LinearLayout remindItemsContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pet_remind, container, false);

        back = view.findViewById(R.id.textView11);
        addRemindButton = view.findViewById(R.id.addRemindButton);
        remindItemsContainer = view.findViewById(R.id.remindItemsContainer);

        // 設置返回按鈕點擊事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetFragment petFragment = new PetFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, petFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // 設置新增提醒按鈕點擊事件
        addRemindButton.setOnClickListener(v -> showAddRemindDialog());

        // 設置寵物名稱
        TextView petName = view.findViewById(R.id.textView3);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            petName.setText(name);
        }

        return view;
    }

    private void showAddRemindDialog() {
        // 創建對話框的 View
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_remind, null);
        TextInputEditText remindTitleInput = dialogView.findViewById(R.id.remindTitleInput);
        TextView dateTimeText = dialogView.findViewById(R.id.dateTimeText);
        MaterialButton selectDateTimeButton = dialogView.findViewById(R.id.selectDateTimeButton);

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        // 設置選擇日期時間按鈕點擊事件
        selectDateTimeButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (datePicker, year, month, day) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        // 選完日期後顯示時間選擇器
                        new TimePickerDialog(getContext(),
                                (timePicker, hour, minute) -> {
                                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                                    calendar.set(Calendar.MINUTE, minute);
                                    dateTimeText.setText(dateFormat.format(calendar.getTime()));
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true)
                                .show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // 顯示新增提醒對話框
        new AlertDialog.Builder(getContext())
                .setTitle("新增提醒事項")
                .setView(dialogView)
                .setPositiveButton("確定", (dialog, which) -> {
                    String title = remindTitleInput.getText().toString();
                    String dateTime = dateTimeText.getText().toString();
                    if (!title.isEmpty() && !dateTime.equals("選擇日期和時間")) {
                        addNewRemindItem(title, dateTime);
                    } else {
                        Toast.makeText(getContext(), "請填寫完整資訊", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void addNewRemindItem(String title, String dateTime) {
        // 動態創建新的提醒項目視圖
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.remind_item, null);

        // 設置提醒標題
        TextView titleText = itemView.findViewById(R.id.remindTitle);
        titleText.setText(title);

        // 設置提醒時間
        TextView dateTimeText = itemView.findViewById(R.id.remindDateTime);
        dateTimeText.setText("下次：" + dateTime);

        // 設置提醒按鈕點擊事件
        ImageButton remindButton = itemView.findViewById(R.id.remindButton);
        remindButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "已設置提醒：" + title, Toast.LENGTH_SHORT).show();
            // 這裡可以添加實際的提醒功能
        });

        // 將新項目添加到容器中
        if (remindItemsContainer != null) {
            remindItemsContainer.addView(itemView, 0); // 添加到列表開頭
        }
    }
}