package com.example.bitalinomonitor.commands;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BitalinoFramesCommand {
    @SerializedName("Identifier")
    @Expose
    public String identifier;

    @SerializedName("Seq")
    @Expose
    public int seq;

    @SerializedName("Analog")
    @Expose
    public int[] analog;

    @SerializedName("Digital")
    @Expose
    public int[] digital;

    public BitalinoFramesCommand(String identifier, int seq, int[] analog, int[] digital){
        this.identifier = identifier;
        this.seq = seq;
        this.analog = analog;
        this.digital = digital;
    }
}
