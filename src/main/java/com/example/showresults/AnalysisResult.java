package com.example.showresults;

public class AnalysisResult {
    private double avg;
    private int max;
    private int min;
    public AnalysisResult(){}

    public AnalysisResult(double avg, int max, int min) {
        this.avg = avg;
        this.max = max;
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
