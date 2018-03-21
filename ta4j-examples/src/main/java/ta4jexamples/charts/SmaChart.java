package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.data.time.TimeSeriesCollection;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class SmaChart extends AbstractChart
{
    
    /**
     * 
     * @param series        TimeSeries          - The Series of ticks
     * @param smaIndexes    SmaIndicator Array  - Wanted SMA Indicators.
     */
    SmaChart(TimeSeries series, int... smaIndexes)
    {
      
        ClosePriceIndicator closePrice    = new ClosePriceIndicator(series);
        SMAIndicator SMA;
        
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));
        
        // When more than 1 SMA indicator is required, keep adding SMA indicators to chart.
        for (int smaIndex : smaIndexes) {
            SMA = new SMAIndicator(closePrice, smaIndex);
            mainGraph.addSeries(buildTimeSeries(series, SMA, SMA.toString(), smaIndex));
        }

        chart = basicChart("SMA Chart", "Date", "Stock Value", mainGraph);
    }
    
  
    /**
     * 
     * @param series        TimeSeries          - The Series of ticks
     * @param closePrice    ClosePriceIndicator - close price reference indicator.
     * @param smaIndicators SMAIndicator Array  - Wanted SMA Indicators.
     */
    SmaChart(TimeSeries series, ClosePriceIndicator closePrice, SMAIndicator... smaIndicators)
    {
      
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));
      
        // When more than 1 SMA indicator is required, keep adding SMA indicators to chart.
        for (SMAIndicator smaIndicator : smaIndicators)
            mainGraph.addSeries(buildTimeSeries(series, smaIndicator, smaIndicator.toString(), smaIndicator.getTimeFrame()));

        chart = basicChart("SMA Chart", "Date", "Stock Value", mainGraph);
    }

	
    //===============================
    
    
    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);
        strategy.setUnstablePeriod(100);

        ClosePriceIndicator closePrice      = new ClosePriceIndicator(series);
        SMAIndicator SMA                    = new SMAIndicator(closePrice, 15);
       
        /**
         * Displaying the chart
         */
        int[] SMAs = {50,80,100};
        Chart SmaChart1 = new SmaChart(series, SMAs);
        Chart SmaChart2 = new SmaChart(series,closePrice,SMA);
        SmaChart1.addBuySellSignals(series, strategy);
        SmaChart2.addBuySellSignals(series, strategy);
        
        displayChart("Stock Stats" , SmaChart1.getChart());
        displayChart("Stock Stats" , SmaChart2.getChart());
    }
	
}
