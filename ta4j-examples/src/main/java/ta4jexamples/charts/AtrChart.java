package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import eu.verdelhan.ta4j.indicators.helpers.TrueRangeIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.trackers.WilderSmoothIndicator;
import ta4jexamples.loaders.CsvTradesLoader;

/*
 * Average true Range Chart.
 */
public class AtrChart extends AbstractChart
{

    /**
    *  @param series 			TimeSeries.
  	*  @param wilderAvgIndex 	Time frame.
  	*  @return ATR Chart.
  	*/
	AtrChart(TimeSeries series, int wilderAvgIndex)
    {
        ClosePriceIndicator closePrice 	    = new ClosePriceIndicator(series);
        
        TrueRangeIndicator trIndicator      = new TrueRangeIndicator(series);
        WilderSmoothIndicator atrIndicator  = new WilderSmoothIndicator(trIndicator, wilderAvgIndex);

        // Add Directional movements to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, trIndicator, trIndicator.toString()));
        mainGraph.addSeries(buildTimeSeries(series, atrIndicator, trIndicator.toString() +" "+ wilderAvgIndex, wilderAvgIndex));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("ATR Chart", "Date", "Price Change", mainGraph, secondaryGraph);
    }
    
	/**
    *  @param series 			TimeSeries.
    *  @param closePrice 		ClosePriceIndicator - Stock Value.
    *  @param trIndicator 		TrueRangeIndicator.
  	*  @param atrIndicator 		WilderSmoothIndicator - The average value by Wilder Smoothing Technique.
  	*  @return ATR Chart
  	*/
	AtrChart(TimeSeries series, ClosePriceIndicator closePrice, TrueRangeIndicator trIndicator, WilderSmoothIndicator atrIndicator)
    {
	    int calculatedAvg = atrIndicator.getTimeFrame();
	    
        // Add Directional movements to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, trIndicator, trIndicator.toString()));
        mainGraph.addSeries(buildTimeSeries(series, atrIndicator, trIndicator.toString() +" "+ calculatedAvg, calculatedAvg));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("ATR Chart", "Date", "Price Change", mainGraph, secondaryGraph);
    }


    //===============================


    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");

        ClosePriceIndicator closePrice 			= new ClosePriceIndicator(series);
        TrueRangeIndicator trIndicator  		= new TrueRangeIndicator(series);
        WilderSmoothIndicator atrIndicator   	= new WilderSmoothIndicator(trIndicator, 14);
        
        Chart AtrChart1 = new AtrChart(series, 14);
        Chart AtrChart2 = new AtrChart(series,closePrice,trIndicator,atrIndicator);
        
        /**
         * Displaying the chart
         */
        displayChart("Stock Stats" , AtrChart1.getChart());
        displayChart("Stock Stats" , AtrChart2.getChart());
    }

}
