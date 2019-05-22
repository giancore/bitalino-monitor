package com.example.bitalinomonitor.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.activities.DeviceActivity;
import com.example.bitalinomonitor.activities.ExamListActivity;
import com.example.bitalinomonitor.activities.PatientActivity;
import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.models.PatientModel;
import com.example.bitalinomonitor.network.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsViewHolder> {
    private final List<PatientModel> patients;
    private RetrofitConfig retrofitConfig;

    public PatientsAdapter(List<PatientModel> patients) {
        this.patients = patients;
        this.retrofitConfig = new RetrofitConfig();
    }

    @NonNull
    @Override
    public PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_patientitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsViewHolder holder, int position) {
        PatientModel patient = patients.get(position);

        holder.itemView.setOnClickListener(v -> goToDetails(v, patient));
        holder.name.setText(patient.getName());
        holder.telephone.setText(patient.getTelephone());
        String photoPath = patient.getPhotoPath();

        if (photoPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            holder.photo.setImageBitmap(bitmapReduzido);
            holder.photo.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        holder.buttonViewOption.setOnClickListener((view) -> {
            PopupMenu popup = new PopupMenu(view.getContext(), holder.buttonViewOption);
            popup.inflate(R.menu.menu_patient_list);
            popup.setOnMenuItemClickListener((MenuItem item) -> {
                switch (item.getItemId()) {
                    case R.id.menu_patient_list_1:
                        goToDetails(view, patient);
                        break;
                    case R.id.menu_patient_list_2:
                        goToDelete(view, patient);
                        break;
                    case R.id.menu_patient_list_3:
                        goToAddExam(view, patient);
                        break;
                    case R.id.menu_patient_list_4:
                        goToViewExams(view, patient);
                        break;
                }
                return false;
            });
            //displaying the popup
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return patients == null ? 0 : patients.size();
    }

    private void goToDetails(View view, PatientModel patient){
        Intent intent = new Intent(view.getContext(), PatientActivity.class);
        intent.putExtra("idPatient", patient.getId());
        view.getContext().startActivity(intent);
    }

    private void goToDelete(View view, PatientModel patient){
        Call<CommandResult> call = retrofitConfig.getPatientService().deletePatient(patient.getId());
        call.enqueue(new Callback<CommandResult>()
        {
            @Override
            public void onResponse(Call<CommandResult> call, Response<CommandResult> response) {
                String message = response.body().message;
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<CommandResult> call, Throwable t) {
                Toast.makeText(view.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToViewExams(View view, PatientModel patient){
        Intent intent = new Intent(view.getContext(), ExamListActivity.class);
        intent.putExtra("PATIENT", patient);
        view.getContext().startActivity(intent);
    }

    private void goToAddExam(View view, PatientModel patient){
        Intent intent = new Intent(view.getContext(), DeviceActivity.class);
        intent.putExtra("PATIENT", patient);
        view.getContext().startActivity(intent);
    }
}
