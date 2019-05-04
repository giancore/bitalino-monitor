package com.example.bitalinomonitor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

import info.plux.pluxapi.bitalino.BITalinoFrame;

public class FrameModel implements Serializable {

    @SerializedName(value = "Id", alternate = { "id" })
    @Expose
    private UUID id;

    @SerializedName(value = "Identifier", alternate = { "identifier" })
    @Expose
    private String identifier;

    @SerializedName(value = "Seq", alternate = { "seq" })
    @Expose
    private int seq;

    @SerializedName(value = "Analog", alternate = { "analog" })
    @Expose
    private int[] analog = new int[6];

    @SerializedName(value = "Digital", alternate = { "digital" })
    @Expose
    private int[] digital = new int[4];

    public String getIdentifier() {
        return identifier;
    }

    public int getSeq() {
        return seq;
    }

    public int[] getAnalogArray() {
        return analog;
    }
    public int getAnalog(int pos) {
        return analog[pos];
    }

    public int[] getDigital() {
        return digital;
    }

    public void setDigital(int[] digital) {
        this.digital = digital;
    }

    public FrameModel(BITalinoFrame bITalinoFrame){
        this.identifier = bITalinoFrame.getIdentifier();
        this.seq = bITalinoFrame.getSequence();
        this.analog = bITalinoFrame.getAnalogArray();
        this.digital = bITalinoFrame.getDigitalArray();
    }
}
