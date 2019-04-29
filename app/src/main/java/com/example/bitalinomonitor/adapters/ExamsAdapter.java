package com.example.bitalinomonitor.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.activities.GraphViewActivity;
import com.example.bitalinomonitor.models.ExamModel;

import java.util.List;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsViewHolder> {
    private final List<ExamModel> exams;

    public ExamsAdapter(List<ExamModel> exams) {
        this.exams = exams;
    }

    @NonNull
    @Override
    public ExamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExamsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_examitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExamsViewHolder holder, int position) {
        ExamModel exam = exams.get(position);

        holder.itemView.setOnClickListener(v -> goToDetails(v, exam));
        holder.examType.setText(exam.getName());
        holder.examDate.setText(exam.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return exams == null ? 0 : exams.size();
    }

    private void goToDetails(View view, ExamModel exam){
        Intent intent = new Intent(view.getContext(), GraphViewActivity.class);
        intent.putExtra("exam", exam);

        view.getContext().startActivity(intent);
    }
}
