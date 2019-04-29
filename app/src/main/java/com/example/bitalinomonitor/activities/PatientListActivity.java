package com.example.bitalinomonitor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.adapters.PatientsAdapter;
import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.commands.ListPatientQueryResult;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientListActivity extends AppCompatActivity {
    private PatientsAdapter adapter;
    private RetrofitConfig retrofitConfig;

    @BindView(R.id.list_patient)
    RecyclerView patientList;

    @BindView(R.id.button_new_patient)
    Button btnNewPatient;

    private List<PatientModel> patients = new ArrayList<PatientModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);
        ButterKnife.bind(this);

        retrofitConfig = new RetrofitConfig();
        patientList.setLayoutManager(new LinearLayoutManager(this));
        patientList.setHasFixedSize(true);
        patientList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        btnNewPatient.setOnClickListener(v -> {
            Intent intentVaiProFormulario = new Intent(PatientListActivity.this, PatientActivity.class);
            startActivity(intentVaiProFormulario);
        });

        registerForContextMenu(patientList);
    }

    private void carregaLista() {
        patients.clear();
        adapter = new PatientsAdapter(patients);
        patientList.setAdapter(adapter);

        Call<List<ListPatientQueryResult>> call = retrofitConfig.getPatientService().getPatients();
        call.enqueue(new Callback<List<ListPatientQueryResult>>()
        {
            @Override
            public void onResponse(Call<List<ListPatientQueryResult>> call, Response<List<ListPatientQueryResult>> response) {
                List<ListPatientQueryResult> body = response.body();

                for(ListPatientQueryResult patient : body) {
                    PatientModel model = new PatientModel(patient.id, patient.name, patient.phone, patient.photoPath);
                    patients.add(model);
                }

                adapter = new PatientsAdapter(patients);
                patientList.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<ListPatientQueryResult>> call, Throwable t) {
                Toast.makeText(PatientListActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }
}
