package com.example.bitalinomonitor.models;

import java.util.UUID;

import info.plux.pluxapi.bitalino.BITalinoFrame;

public class FrameModel {
    private UUID id;
    private String identifier;
    private int seq;
    private int[] analog = new int[6];
    private int[] digital = new int[4];

    public String getIdentifier() {
        return identifier;
    }

    public int getSeq() {
        return seq;
    }

    public int[] getAnalog() {
        return analog;
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
