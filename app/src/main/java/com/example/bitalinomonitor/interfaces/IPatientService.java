package com.example.bitalinomonitor.interfaces;

import com.example.bitalinomonitor.commands.CommandResult;
import com.example.bitalinomonitor.commands.CreatePatientCommand;
import com.example.bitalinomonitor.commands.GetPatientExamQueryResult;
import com.example.bitalinomonitor.commands.GetPatientQueryResult;
import com.example.bitalinomonitor.commands.ListPatientExamsQueryResult;
import com.example.bitalinomonitor.commands.ListPatientQueryResult;
import com.example.bitalinomonitor.models.FrameModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IPatientService {
    @FormUrlEncoded
    @POST("v1/Patients/Exam")
    Call saveExam(@Field("IdPatient") UUID idPatient, @Field("Date") Date date, @Field("Channel") int channel, @Field("Frames") ArrayList<FrameModel> frames);

    @POST("v1/Patients")
    Call<CommandResult> savePatient(@Body CreatePatientCommand command);

    @GET("v1/Patients")
    Call<List<ListPatientQueryResult>> getPatients();

    @GET("v1/Patients/{id}")
    Call<GetPatientQueryResult> getPatient(@Path("id") UUID idPatient);

    @GET("v1/Patients/{idPatient}/Exams")
    Call<ListPatientExamsQueryResult> getExams(@Path("idPatient") UUID idPatient);

    @GET("v1/Patients/Exam/{idExam}")
    Call<GetPatientExamQueryResult> getExam(@Path("idExam") UUID idExam);
}
