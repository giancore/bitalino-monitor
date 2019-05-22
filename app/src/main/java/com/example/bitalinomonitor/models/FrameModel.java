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
    private double[] analog = new double[6];

    @SerializedName(value = "Digital", alternate = { "digital" })
    @Expose
    private double[] digital = new double[4];

    public String getIdentifier() {
        return identifier;
    }

    public int getSeq() {
        return seq;
    }

    public double[] getAnalogArray() {
        return analog;
    }
    public double getAnalog(int pos) {
        return analog[pos];
    }

    public double[] getDigital() {
        return digital;
    }

    public void setDigital(double[] digital) {
        this.digital = digital;
    }

    public FrameModel(BITalinoFrame bITalinoFrame){
        this.identifier = bITalinoFrame.getIdentifier();
        this.seq = bITalinoFrame.getSequence();

        for(int i=0; i < bITalinoFrame.getAnalogArray().length; i++) {
            this.analog[i] = bITalinoFrame.getAnalogArray()[i];
        }

        for(int i=0; i < bITalinoFrame.getDigitalArray().length; i++) {
            this.digital[i] = bITalinoFrame.getDigitalArray()[i];
        }

        //this.analog = bITalinoFrame.getAnalogArray();
        //this.digital = bITalinoFrame.getDigitalArray();
    }
}
