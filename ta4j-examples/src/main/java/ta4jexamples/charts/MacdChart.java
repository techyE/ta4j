package ta4jexamples.charts;

import java.awt.Color;
import java.text.SimpleDateFormat;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import eu.verdelhan.ta4j.indicators.trackers.MACDIndicator;
import ta4jexamples.loaders.CsvTradesLoader;

public class MacdChart extends AbstractChart
{

    /**
     *
     * @param series            TimeSeries
     * @param shortEmaIndex     int         - EMA Index short term.
     * @param longEmaIndex      int         - EMA Index long term.
     * @param macdEmaIndex      int         - MACD for EMA index.
     */
    MacdChart(TimeSeries series, int shortEmaIndex, int longEmaIndex, int macdEmaIndex)
    {
        ClosePriceIndicator closePrice  = new ClosePriceIndicator(series);
        MACDIndicator macd              = new MACDIndicator(closePrice, shortEmaIndex, longEmaIndex);
        EMAIndicator  macdEma           = new EMAIndicator(macd, macdEmaIndex);
        
        int calculatedAvg = macd.getLongTermEma();
        
        // Add MACD movements to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, macd, "MACDIndicator " + macd.getShortTermEma() + "," + macd.getLongTermEma(), calculatedAvg));
        mainGraph.addSeries(buildTimeSeries(series, macdEma, "MACD " + macdEma.toString(), calculatedAvg));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));
        
        buildChart(mainGraph, secondaryGraph);
       
    }

    /**
     *
     * @param series           TimeSeries
     * @param closePrice       ClosePriceIndicator
     * @param macd             MACDIndicator
     * @param macdEma          EMAIndicator         - EMA for MACD.
     */
    MacdChart(TimeSeries series, ClosePriceIndicator closePrice, MACDIndicator macd, EMAIndicator  macdEma )
    {
        int calculatedAvg = macd.getLongTermEma();
        
        // Add MACD movements to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, macd,  "MACDIndicator " + macd.getShortTermEma() + "," + macd.getLongTermEma(), calculatedAvg));
        mainGraph.addSeries(buildTimeSeries(series, macdEma, "MACD " + macdEma.toString(), calculatedAvg));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        buildChart(mainGraph, secondaryGraph);
    }
    
    /**
     * Building the chart according to given graphs.
     * @param mainGraph         TimeSeriesCollection    - MACD indicator.
     * @param secondaryGraph    TimeSeriesCollection    - Close Prices Indicator.
     */
    private void buildChart(TimeSeriesCollection mainGraph, TimeSeriesCollection secondaryGraph)
    {   
        // Set margin line for indicating crossing.
        Marker zeroLine    = new ValueMarker(0);
        zeroLine.setPaint(Color.BLACK);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "MACD",        // title
                "Date",             // x-axis label
                "Change %",         // y-axis label
                mainGraph,          // data
                true,               // create legend?
                true,               // generate tooltips?
                false               // generate URLs?
                );
        XYPlot plot     = (XYPlot) chart.getPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.addRangeMarker(zeroLine);
        // Edit Background.
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);
        // X axis by tick date.
        DateAxis axisX   = (DateAxis) plot.getDomainAxis();
        axisX.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));
        // Add Axis
        addSecondaryAxis(plot, secondaryGraph, "Stock Value");
        
        this.chart =  chart;
    }


    //===============================
    
    
    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");

        ClosePriceIndicator closePrice  = new ClosePriceIndicator(series);
        MACDIndicator macd              = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator  macdEma           = new EMAIndicator(macd, 9);
        
        Chart MACD1 = new MacdChart(series,12,26,9);
        Chart MACD2 = new MacdChart(series,closePrice,macd,macdEma);
        
        /**
         * Displaying the chart
         */
        displayChart("Stock Stats" , MACD1.getChart());
        displayChart("Stock Stats" , MACD2.getChart());
    }

}