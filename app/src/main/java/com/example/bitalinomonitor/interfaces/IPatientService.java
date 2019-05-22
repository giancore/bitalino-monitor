package com.example.bitalinomonitor.interfaces;

import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.commands.CreatePatientCommand;
import com.example.bitalinomonitor.commands.GetExamCommandResult;
import com.example.bitalinomonitor.commands.GetPatientQueryResult;
import com.example.bitalinomonitor.commands.ListPatientExamsQueryResult;
import com.example.bitalinomonitor.commands.ListPatientQueryResult;
import com.example.bitalinomonitor.models.ExamModel;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IPatientService {
    @POST("v1/Patients/Exam")
    Call<CommandResult> saveExam(@Body ExamModel command);

    @POST("v1/Patients")
    Call<CommandResult> savePatient(@Body CreatePatientCommand command);

    @DELETE("v1/Patients/{id}")
    Call<CommandResult> deletePatient(@Path("id") UUID idPatient);

    @GET("v1/Patients")
    Call<List<ListPatientQueryResult>> getPatients();

    @GET("v1/Patients/{id}")
    Call<GetPatientQueryResult> getPatient(@Path("id") UUID idPatient);

    @GET("v1/Patients/{idPatient}/Exams")
    Call<List<ListPatientExamsQueryResult>> getExams(@Path("idPatient") UUID idPatient);

    @GET("v1/Patients/Exam/{idExam}")
    Call<GetExamCommandResult> getExam(@Path("idExam") UUID idExam);

    @GET("v1/Patients/Exam/{idExam}/Create-Medical-Records")
    Call<CommandResult> getMedicalRecords(@Path("idExam") UUID idExam);

    @GET("v1/Patients/Exam/{idExam}/Transform-Medical-Records")
    Call<CommandResult> transformMedicalRecordsToHTML(@Path("idExam") UUID idExam);

    @GET("v1/Patients/Exam/{idExam}/Filter")
    Call<CommandResult> getFilterExamResult(@Path("idExam") UUID idExam);
}
