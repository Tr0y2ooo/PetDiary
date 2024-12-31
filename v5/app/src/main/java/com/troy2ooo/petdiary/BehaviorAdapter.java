package com.troy2ooo.petdiary;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import java.util.List;
import java.util.ArrayList;
import androidx.annotation.NonNull;



public class BehaviorAdapter extends RecyclerView.Adapter<BehaviorAdapter.ViewHolder> {

    private List<String> behaviors;  // 用來存儲行為的資料

    public BehaviorAdapter(List<String> behaviors) {
        this.behaviors = behaviors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 在這裡加載每個項目的佈局（每個行為的LinearLayout）
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String behavior = behaviors.get(position);

        holder.textView.setText(behavior);  // 設置行為名稱
        // 可以在這裡對每個項目進行進一步的設置
    }

    @Override
    public int getItemCount() {
        return behaviors.size();  // 返回列表大小
    }

    // ViewHolder 類型
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;  // 用來顯示每個行為

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.trainingName);  // 在行為項目的佈局中找到 TextView
        }
    }
}