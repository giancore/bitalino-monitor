package com.example.bitalinomonitor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.adapters.PatientsAdapter;
import com.example.bitalinomonitor.models.PatientModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientListActivity extends AppCompatActivity {
    private PatientsAdapter adapter;

    @BindView(R.id.patient_list)
    RecyclerView patientList;

    @BindView(R.id.novo_aluno)
    Button novoAluno;

    private List<PatientModel> patients = new ArrayList<PatientModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);
        ButterKnife.bind(this);

        patientList.setLayoutManager(new LinearLayoutManager(this));
        patientList.setHasFixedSize(true);

        carregaLista();

        patientList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        novoAluno.setOnClickListener(v -> {
            Intent intentVaiProFormulario = new Intent(PatientListActivity.this, PatientActivity.class);
            startActivity(intentVaiProFormulario);
        });

        registerForContextMenu(patientList);
    }

    private void carregaLista() {
        //AlunoDAO dao = new AlunoDAO(this);
        //List<PatientModel> patients = dao.buscaAlunos();
        //dao.close();

        patients.clear();

        PatientModel patient1 = new PatientModel();
        patient1.setId((long)1);
        patient1.setName("Paciente 1");
        patient1.setTelephone("(51) 98184-1977");
        patients.add(patient1);

        PatientModel patient2 = new PatientModel();
        patient2.setId((long)2);
        patient2.setName("Paciente 2");
        patient2.setTelephone("(51) 98184-1977");
        patients.add(patient2);

        PatientModel patient3 = new PatientModel();
        patient3.setId((long)3);
        patient3.setName("Paciente 3");
        patient3.setTelephone("(51) 98184-1977");
        patients.add(patient3);

        adapter = new PatientsAdapter(this, patients);
        patientList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }
}
