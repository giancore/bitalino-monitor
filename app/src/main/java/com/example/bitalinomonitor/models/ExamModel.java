package com.example.bitalinomonitor.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ExamModel implements Serializable {
    private UUID id;
    private UUID idPatient;
    private Date date;
    private int channel;
    private int type;
    private ArrayList<FrameModel> frames;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return "";
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UUID getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(UUID idPatient) {
        this.idPatient = idPatient;
    }

    public ArrayList<FrameModel> getFrames() {
        return frames;
    }

    public void setFrames(ArrayList<FrameModel> frames) {
        this.frames = frames;
    }
}
