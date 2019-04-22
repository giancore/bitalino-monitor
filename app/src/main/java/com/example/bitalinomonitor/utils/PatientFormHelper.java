package com.example.bitalinomonitor.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bitalinomonitor.R;
import com.example.bitalinomonitor.activities.PatientActivity;
import com.example.bitalinomonitor.models.PatientModel;

public class PatientFormHelper {

    private final EditText fieldName;
    private final EditText fieldTelephone;
    private final ImageView fieldPhoto;

    private PatientModel patient;

    public PatientFormHelper(PatientActivity activity) {
        fieldName = activity.findViewById(R.id.patient_name);
        fieldTelephone = activity.findViewById(R.id.patient_phone);
        fieldPhoto = activity.findViewById(R.id.formulario_foto);
        patient = new PatientModel();
    }

    public PatientModel getPatient() {
        patient.setName(fieldName.getText().toString());
        patient.setTelephone(fieldTelephone.getText().toString());
        patient.setPhotoPath((String) fieldPhoto.getTag());
        return patient;
    }

    public void fillForm(PatientModel aluno) {
        fieldName.setText(aluno.getName());
        fieldTelephone.setText(aluno.getTelephone());
        loadImage(aluno.getPhotoPath());
        this.patient = aluno;
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
