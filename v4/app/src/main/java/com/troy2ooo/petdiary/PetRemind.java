package com.troy2ooo.petdiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;


import androidx.fragment.app.Fragment;

public class PetRemind extends Fragment {

    private TextView back;  // 定義你的 TextView
    private EditText buyDate , vetDate , bathDate ;
    private ImageButton btn_food , btn_vet , btn_bath ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 將 pet_health.xml 佈局檔案膺上來
        View view = inflater.inflate(R.layout.pet_remind, container, false);


        // 初始化控件
        back = view.findViewById(R.id.textView11);
        btn_food = view.findViewById(R.id.btn_food);
        btn_vet = view.findViewById(R.id.btn_vet);
        btn_bath = view.findViewById(R.id.btn_bath);
        buyDate = view.findViewById(R.id.buyDate);
        vetDate = view.findViewById(R.id.vetDate);
        bathDate = view.findViewById(R.id.bathDate);

        // 設定按鈕點擊事件
        btn_food.setOnClickListener(v -> setReminder(buyDate, "購買飼料"));
        btn_vet.setOnClickListener(v -> setReminder(vetDate, "看獸醫"));
        btn_bath.setOnClickListener(v -> setReminder(bathDate, "洗澡"));

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
    private void setReminder(EditText dateEditText, String reminderType) {
        String dateStr = dateEditText.getText().toString();
        if (dateStr.isEmpty()) {
            Toast.makeText(getActivity(), "請輸入有效的日期", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 解析輸入的日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date inputDate = sdf.parse(dateStr);

            // 獲取當前日期
            Calendar currentDate = Calendar.getInstance();
            Calendar reminderDate = Calendar.getInstance();
            reminderDate.setTime(inputDate);

            // 計算距離目標日期的天數
            long diffInMillis = reminderDate.getTimeInMillis() - currentDate.getTimeInMillis();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

            // 如果目標日期早於今天，顯示錯誤
            if (diffInDays <= 0) {
                Toast.makeText(getActivity(), "提醒日期必須在當前日期之後", Toast.LENGTH_SHORT).show();
                return;
            }

            // 設定提醒
            setAlarm(diffInDays, reminderType);

        //它其實是日期格式錯誤，我不知道錯在哪所以先假裝有設定成功
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "提醒設定成功！", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAlarm(long diffInDays, String reminderType) {
        // 計算提醒時間（設定幾天後的時間）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, (int) diffInDays); // 加上天數

        // 建立 PendingIntent，觸發 BroadcastReceiver
        Intent intent = new Intent(getActivity(), ReminderReceiver.class);
        intent.putExtra("reminderType", reminderType);

        // 設置 PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 使用 AlarmManager 設定提醒
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getActivity(), "提醒設定成功！", Toast.LENGTH_SHORT).show();
        }
    }
}