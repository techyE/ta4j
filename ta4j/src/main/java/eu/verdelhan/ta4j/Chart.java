package eu.verdelhan.ta4j;

import org.jfree.chart.JFreeChart;

public interface Chart {

    // Adds Lines to indicate Enter/Exit point in chart.
    void addBuySellSignals(TimeSeries series, Strategy strategy);

    // Adds Signal that indicate rule satisfaction.
    void addSatisfiedSignals(TimeSeries series, Rule rule);
    void addSatisfiedSignals(TimeSeries series, Strategy strategy);

    // Get Chart Object.
    JFreeChart getChart();

}
