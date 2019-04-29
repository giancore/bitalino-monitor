package com.example.bitalinomonitor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.activities.PatientActivity;
import com.example.bitalinomonitor.models.PatientModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientFormHelper {

    private final EditText fieldName;
    private final EditText fieldTelephone;
    private final EditText fieldDateOfBirth;
    private final ImageView fieldPhoto;

    private PatientModel patient;

    public PatientFormHelper(PatientActivity activity) {
        fieldName = activity.findViewById(R.id.patient_name);
        fieldTelephone = activity.findViewById(R.id.patient_phone);
        fieldPhoto = activity.findViewById(R.id.formulario_foto);
        fieldDateOfBirth = activity.findViewById(R.id.patient_date_of_birth);
        patient = new PatientModel();
    }

    public PatientModel getPatient() {
        patient.setName(fieldName.getText().toString());
        patient.setTelephone(fieldTelephone.getText().toString());
        patient.setPhotoPath((String) fieldPhoto.getTag());

        try {
            String date = fieldDateOfBirth.getText().toString();

            Date dateOfBirth = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR")).parse(date);
            patient.setDateOfBirth(dateOfBirth);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return patient;
    }

    public void fillForm(PatientModel patient) {
        fieldName.setText(patient.getName());
        fieldTelephone.setText(patient.getTelephone());
        fieldDateOfBirth.setText(patient.getDateOfBirthAsString());
        loadImage(patient.getPhotoPath());
        this.patient = patient;
    }

    public void loadImage(String caminhoFoto) {
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            fieldPhoto.setImageBitmap(bitmapReduzido);
            fieldPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
            fieldPhoto.setTag(caminhoFoto);
        }
    }
}
