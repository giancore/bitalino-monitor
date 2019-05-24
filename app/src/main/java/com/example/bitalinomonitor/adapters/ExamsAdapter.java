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
import com.example.bitalinomonitor.models.PatientModel;

import java.util.List;

public class ExamsAdapter extends RecyclerView.Adapter<ExamsViewHolder> {
    private final List<ExamModel> exams;
    private final PatientModel patient;

    public ExamsAdapter(List<ExamModel> exams, PatientModel patient) {
        this.exams = exams;
        this.patient = patient;
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
        String examType = String.format("%s - %sHz", exam.getName(), exam.getFrequency());

        holder.itemView.setOnClickListener(v -> goToDetails(v, exam));
        holder.examType.setText(examType);
        holder.examDate.setText(exam.getDateAsString());
    }

    @Override
    public int getItemCount() {
        return exams == null ? 0 : exams.size();
    }

    private void goToDetails(View view, ExamModel exam){
        Intent intent = new Intent(view.getContext(), GraphViewActivity.class);
        intent.putExtra("IDEXAM", exam.getId());
        intent.putExtra("PATIENT", patient);

        view.getContext().startActivity(intent);
    }
}
