package com.example.bitalinomonitor.utils;

public class SensorTransferFunctions {

    private static final int gainEcg = 1100;
    private static final int gainEcmg = 1009;
    private static final int gainEeg = 40000;
    private static final double vcc = 3.3;

    /*
    * The number of bits for each channel depends on the resolution of the Analog-to-Digital Converter (ADC); in
    * BITalino the first four channels are sampled using 10-bit resolution (n = 10), while the last two may be sampled using
    * 6-bit (n = 6).*/
    private static final int n = 10;

    public static double calculateElectrocardiographyValue(double adc) {
        double partOne = (adc/Math.pow(2, n)) - 0.5;
        double result = (partOne * vcc) / gainEcg;

        double resultInMilliVolts = result * 1000;

        return resultInMilliVolts;
    }

    public static double calculateElectrodermalActivityValue(double adc) {
        double constant = 0.132;
        double partOne = (adc/Math.pow(2, n));
        double resultInMicroSeconds = (partOne * vcc) / constant;

        return resultInMicroSeconds;
    }

    public static double calculateElectromyographyValue(double adc) {
        double partOne = (adc/Math.pow(2, n)) - 0.5;
        double result = (partOne * vcc) / gainEcmg;

        double resultInMilliVolts = result * 1000;

        return resultInMilliVolts;
    }

    public static double calculateElectroencephalographyValue(double adc) {
        double partOne = (adc/Math.pow(2, n)) - 0.5;
        double result = (partOne * vcc) / gainEeg;

        double resultInMicroVolts = result * 0.000001;

        return resultInMicroVolts;
    }
}
