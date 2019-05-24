package com.example.bitalinomonitor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.adapters.ExamsAdapter;
import com.example.bitalinomonitor.commands.ListPatientExamsQueryResult;
import com.example.bitalinomonitor.models.ExamModel;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamListActivity extends AppCompatActivity {
    private ExamsAdapter adapter;
    private RetrofitConfig retrofitConfig;
    private PatientModel patient;
    private List<ExamModel> exams = new ArrayList<>();

    @BindView(R.id.list_exams)
    RecyclerView examsList;

    @BindView(R.id.list_exams_progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examlist);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        patient = (PatientModel) intent.getSerializableExtra("PATIENT");

        retrofitConfig = new RetrofitConfig();
        examsList.setLayoutManager(new LinearLayoutManager(this));
        examsList.setHasFixedSize(true);
        examsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        registerForContextMenu(examsList);
    }

    private void loadList() {
        exams.clear();
        progressBar.setVisibility(View.VISIBLE);

        Call<List<ListPatientExamsQueryResult>> call = retrofitConfig.getPatientService().getExams(patient.getId());
        call.enqueue(new Callback<List<ListPatientExamsQueryResult>>()
        {
            @Override
            public void onResponse(Call<List<ListPatientExamsQueryResult>> call, Response<List<ListPatientExamsQueryResult>> response) {
                List<ListPatientExamsQueryResult> body = response.body();

                for(ListPatientExamsQueryResult exam : body) {
                    ExamModel model = new ExamModel();
                    model.setId(exam.id);
                    model.setDate(exam.date);
                    model.setChannel(exam.channel);
                    model.setFrequency(exam.frequency);
                    exams.add(model);
                }

                adapter = new ExamsAdapter(exams, patient);
                examsList.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<List<ListPatientExamsQueryResult>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ExamListActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }
}
