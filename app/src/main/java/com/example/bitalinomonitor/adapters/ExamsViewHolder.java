package com.example.bitalinomonitor.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_exam_type)
    TextView examType;

    @BindView(R.id.textview_exam_date)
    TextView examDate;

    public ExamsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
