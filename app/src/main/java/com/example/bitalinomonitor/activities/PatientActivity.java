package com.example.bitalinomonitor.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.commands.CreatePatientCommand;
import com.example.bitalinomonitor.commands.GetPatientQueryResult;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;
import com.example.bitalinomonitor.utils.DateMask;
import com.example.bitalinomonitor.utils.Mask;
import com.example.bitalinomonitor.utils.PatientFormHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 567;

    private UUID idPatient;
    private PatientFormHelper helper;
    private String caminhoFoto;
    private RetrofitConfig retrofitConfig;

    @BindView(R.id.formulario_botao_foto)
    Button botaoFoto;

    @BindView(R.id.patient_name)
    EditText patientName;

    @BindView(R.id.patient_phone)
    EditText  patientPhone;

    @BindView(R.id.patient_date_of_birth)
    EditText patientDateOfBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        ButterKnife.bind(this);

        retrofitConfig = new RetrofitConfig();
        helper = new PatientFormHelper(this);

        patientPhone.addTextChangedListener(Mask.insert("(##)#####-####", patientPhone));
        patientDateOfBirth.addTextChangedListener(new DateMask());
        patientDateOfBirth.setOnClickListener((View v) -> {
            Locale locale = new Locale("pt", "BR");

            final Calendar calendarToday =  Calendar.getInstance(locale);
            int mYear = calendarToday.get(Calendar.YEAR);
            int mMonth = calendarToday.get(Calendar.MONTH);
            int mDay = calendarToday.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance(locale);
                calendar.set(year, monthOfYear,dayOfMonth);
                String selectedDate = new SimpleDateFormat("dd/MM/yyyy", locale).format(calendar.getTime());
                patientDateOfBirth.setText(selectedDate);
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        });

        Intent intent = getIntent();
        idPatient = (UUID) intent.getSerializableExtra("idPatient");

        if (idPatient != null) {
            Call<GetPatientQueryResult> call = retrofitConfig.getPatientService().getPatient(idPatient);
            call.enqueue(new Callback<GetPatientQueryResult>()
            {
                @Override
                public void onResponse(Call<GetPatientQueryResult> call, Response<GetPatientQueryResult> response) {
                    GetPatientQueryResult body = response.body();

                    PatientModel patient = new PatientModel(body.id, body.name, body.phone, body.photoPath);
                    patient.setDateOfBirth(body.dateOfBirth);
                    helper.fillForm(patient);
                }
                @Override
                public void onFailure(Call<GetPatientQueryResult> call, Throwable t) {
                    Toast.makeText(PatientActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

        botaoFoto.setOnClickListener((view) -> {
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
            File arquivoFoto = new File(caminhoFoto);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
            startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                helper.loadImage(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_patient, menu);

        if (idPatient != null) {
            MenuItem item = menu.findItem(R.id.menu2);
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PatientModel patient = helper.getPatient();

        switch (item.getItemId()) {
            case R.id.menu1:
                CreatePatientCommand createPatientCommand = new CreatePatientCommand();
                createPatientCommand.dateOfBirth = patient.getDateOfBirth();
                createPatientCommand.name = patient.getName();
                createPatientCommand.phone = patient.getTelephone();
                createPatientCommand.photoPath = patient.getPhotoPath();
                createPatientCommand.id = idPatient;

                Call<CommandResult> call = retrofitConfig.getPatientService().savePatient(createPatientCommand);
                call.enqueue(new Callback<CommandResult>()
                {
                    @Override
                    public void onResponse(Call<CommandResult> call, Response<CommandResult> response) {
                        boolean isSuccess = response.body().success;
                        if (isSuccess){
                            String message = String.format("Paciente %s salvo!", patient.getName());
                            Toast.makeText(PatientActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String message = response.body().message;
                            Toast.makeText(PatientActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<CommandResult> call, Throwable t) {
                        Toast.makeText(PatientActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                finish();
                break;
            case R.id.menu2:
                goToAddExam(patient);
                break;
            case R.id.menu3:
                goToViewExams(patient);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToAddExam(PatientModel patient){
        Intent intent = new Intent(PatientActivity.this, DeviceActivity.class);
        intent.putExtra("PATIENT", patient);

        startActivity(intent);
    }

    private void goToViewExams(PatientModel patient){
        Intent intent = new Intent(PatientActivity.this, ExamListActivity.class);
        intent.putExtra("PATIENT", patient);

        startActivity(intent);
    }
}
