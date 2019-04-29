package com.example.bitalinomonitor.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.adapters.ExamsAdapter;
import com.example.bitalinomonitor.models.ExamModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExamListActivity extends AppCompatActivity {
    private ExamsAdapter adapter;

    @BindView(R.id.list_exams)
    RecyclerView examsList;

    private List<ExamModel> exams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);
        ButterKnife.bind(this);

        examsList.setLayoutManager(new LinearLayoutManager(this));
        examsList.setHasFixedSize(true);

        carregaLista();

        examsList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        registerForContextMenu(examsList);
    }

    private void carregaLista() {
        //AlunoDAO dao = new AlunoDAO(this);
        //List<PatientModel> exams = dao.buscaAlunos();
        //dao.close();

        exams.clear();

        adapter = new ExamsAdapter(exams);
        examsList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }
}
