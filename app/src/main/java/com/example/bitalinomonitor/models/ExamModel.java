package com.example.bitalinomonitor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import info.plux.pluxapi.bitalino.BITalinoFrame;

public class ExamModel implements Serializable {
    @SerializedName(value = "Id", alternate = { "id" })
    @Expose
    private UUID id;

    @SerializedName(value = "IdPatient", alternate = { "idPatient" })
    @Expose
    private UUID idPatient;

    @SerializedName(value = "Date", alternate = { "date" })
    @Expose
    private Date date;

    @SerializedName(value = "Channel", alternate = { "channel" })
    @Expose
    private int channel;

    @SerializedName(value = "Frames", alternate = { "frames" })
    @Expose
    private List<FrameModel> frames;

    @SerializedName(value = "Duration", alternate = { "duration" })
    @Expose
    private long duration;

    @SerializedName(value = "Frequency", alternate = { "frequency" })
    @Expose
    private int frequency = 1;

    public ExamModel(){
        this.frames = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getName() {
        return new BitalinoConfiguration().getExamNameByChannel(channel);
    }

    public UUID getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(UUID idPatient) {
        this.idPatient = idPatient;
    }

    public List<FrameModel> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameModel> frames) {
        this.frames = frames;
    }

    public void setBitalinoFrames(List<BITalinoFrame> frames) {
        for (BITalinoFrame frame : frames){
            this.frames.add(new FrameModel(frame));
        }
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        if (frequency == 1 || frequency == 10 || frequency == 100 || frequency == 1000) {
            this.frequency = frequency;
        }
    }
}