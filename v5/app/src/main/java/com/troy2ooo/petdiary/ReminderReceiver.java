package com.troy2ooo.petdiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String reminderType = intent.getStringExtra("reminderType");

        // 顯示提醒
        Toast.makeText(context, "提醒: " + reminderType, Toast.LENGTH_LONG).show();

        // 可以在這裡使用 Notification 來顯示通知
    }
}

