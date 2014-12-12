package org.saliya.streams;

import java.util.DoubleSummaryStatistics;
import java.util.stream.IntStream;

public class DoubleSummaryStatisticsContainer {
    private DoubleSummaryStatistics[] summaries;
    private int vecLen = 0;

    public DoubleSummaryStatisticsContainer(int vecLen) {
        this.vecLen = vecLen;
        summaries = new DoubleSummaryStatistics[vecLen];
        IntStream.range(0, vecLen).parallel().forEach(i -> summaries[i] = new DoubleSummaryStatistics());
    }

    public void accept(double [] vec) {
        IntStream.range(0, vecLen).parallel().forEach(i -> summaries[i].accept(vec[i]));
    }
    public void combine(DoubleSummaryStatisticsContainer other) {
        IntStream.range(0, vecLen).parallel().forEach(i -> summaries[i].combine(other.summaries[i]));
    }

    public DoubleSummaryStatistics [] getSummaries(){
        return  summaries;
    }
}
