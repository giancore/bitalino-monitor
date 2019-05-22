package com.example.bitalinomonitor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.network.RetrofitConfig;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalRecordsActivity extends AppCompatActivity {
    private RetrofitConfig retrofitConfig;
    private String html;

    @BindView(R.id.textview_medical_records)
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalrecords);
        ButterKnife.bind(this);
        retrofitConfig = new RetrofitConfig();

        Intent intent = getIntent();
        UUID idexam = (UUID) intent.getSerializableExtra("IDEXAM");
        transformMedicalRecordsToHTML(idexam);
    }

    private void transformMedicalRecordsToHTML(UUID idExam){
        Call<CommandResult> call = retrofitConfig.getPatientService().transformMedicalRecordsToHTML(idExam);
        call.enqueue(new Callback<CommandResult>()
        {
            @Override
            public void onResponse(Call<CommandResult> call, Response<CommandResult> response) {
                boolean isSuccess = response.body().success;

                if(isSuccess) {
                    LinkedTreeMap<String, String> data = (LinkedTreeMap<String, String>) response.body().data;
                    html = data.get("result");

                    textView.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));

                } else {
                    String message = response.body().message;
                    Toast.makeText(MedicalRecordsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CommandResult> call, Throwable t) {
                Toast.makeText(MedicalRecordsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
