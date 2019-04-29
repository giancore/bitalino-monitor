package com.example.bitalinomonitor.models;

import java.util.ArrayList;

public class ExamOptionModel {
    public static String ECG = "Eletrocardiograma (ECG)";
    public static String EEG = "Eletroencefalografia (EEG)";
    public static String EMG = "Eletromiografia (EMG)";
    public static String EDA = "Resposta Galvânica da Pele (EDA)";

    public static int ECG_Channel = 1;
    public static int EEG_Channel = 3;
    public static int EMG_Channel = 0;
    public static int EDA_Channel = 2;

    private String title;
    private int channel;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    private ExamOptionModel(int channel, String title){
        this.channel = channel;
        this.title = title;
    }

    public static ArrayList<ExamOptionModel> getOptions() {
        ArrayList<ExamOptionModel> arrayList = new ArrayList<ExamOptionModel>();

        arrayList.add(new ExamOptionModel(1, EMG));
        arrayList.add(new ExamOptionModel(2, ECG));
        arrayList.add(new ExamOptionModel(3, EDA));
        arrayList.add(new ExamOptionModel(4, EEG));

        return  arrayList;
    }

    public static  ArrayList<String> getOptionsAsString(){
        ArrayList<String> arrayListAsString = new ArrayList<String>();
        ArrayList<ExamOptionModel> arrayList = getOptions();

        for (ExamOptionModel array: arrayList) {
            arrayListAsString.add(array.title);
        }

        return arrayListAsString;
    }
}
