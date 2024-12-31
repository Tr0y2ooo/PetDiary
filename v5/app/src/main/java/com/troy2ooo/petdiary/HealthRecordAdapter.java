package com.troy2ooo.petdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.ViewHolder> {
    private List<HealthRecord> records = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final TextView contentText;

        public ViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.recordDate);
            contentText = view.findViewById(R.id.recordContent);
        }

        public TextView getDateText() {
            return dateText;
        }

        public TextView getContentText() {
            return contentText;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HealthRecord record = records.get(position);
        holder.getDateText().setText(record.getDate());
        holder.getContentText().setText(record.getContent());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void addRecord(HealthRecord record) {
        records.add(0, record); // 添加到列表開頭
        notifyItemInserted(0);
    }
} 