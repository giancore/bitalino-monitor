package com.example.bitalinomonitor.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BitalinoConfiguration {
    Map<Integer, String> exams = new HashMap<Integer, String>();
    Map<Integer, String> frequencies = new HashMap<Integer, String>();

    public static int EMG_Channel = 0;
    public static int ECG_Channel = 1;
    public static int EDA_Channel = 2;
    public static int EEG_Channel = 3;

    public BitalinoConfiguration() {
        this.buildExams();
        this.buildFrequency();
    }

    public String getExamNameByChannel(int channel){
        return this.exams.get(channel);
    }

    public int getChannelByExamName(String examName){
        if (this.exams.containsValue(examName)){
            return getKey(this.exams, examName);
        }

        return 0;
    }

    public int getFrequencyByName(String frequencyName){
        if (this.frequencies.containsValue(frequencyName)){
            return getKey(this.frequencies, frequencyName);
        }

        return 1;
    }

    public ArrayList<String> getExamsAsArray(){
        ArrayList<String> examsArrayString = new ArrayList<>();

        for (String exam: this.exams.values()){
            examsArrayString.add(exam);
        }

        return examsArrayString;
    }

    public ArrayList<String> getFrequenciesAsArray(){
        ArrayList<String> frequenciesArrayString = new ArrayList<>();

        for (String rate: this.frequencies.values()){
            frequenciesArrayString .add(rate);
        }

        return frequenciesArrayString ;
    }

    public Map<Integer, String> getExams() {
        return exams;
    }

    private void buildExams() {
        this.exams.put(EMG_Channel, "Eletromiografia (EMG)");
        this.exams.put(ECG_Channel, "Eletrocardiograma (ECG)");
        this.exams.put(EDA_Channel, "Resposta Galvânica da Pele (EDA)");
        this.exams.put(EEG_Channel, "Eletroencefalografia (EEG)");
    }

    private void buildFrequency() {
        this.frequencies.put(1, "1 Hz");
        this.frequencies.put(10, "10 Hz");
        this.frequencies.put(100, "100 Hz");
        this.frequencies.put(1000, "1000 Hz");
    }

    private static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
