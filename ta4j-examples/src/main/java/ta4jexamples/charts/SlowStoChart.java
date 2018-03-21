package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import ta4jexamples.loaders.CsvHistoricalLoader;
import ta4jexamples.loaders.CsvLoader;
import ta4jexamples.scraperPaths.YahooHistCsvPath;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeriesCollection;

import org.joda.time.DateTime;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorDIndicator;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import ta4jexamples.strategies.MovingMomentumStrategy;


public class SlowStoChart extends AbstractChart
{

    /**
    *  @param series TimeSeries.
  	*  @param kTimeFrame Int - K indicator.
  	*  @param dTimeFrame Int - D indicator.
  	*  @param overSoldIndex Int - Indication for 'OverSold'.
  	*  @param overBoughtIndex Int - indication for 'OverBought'.
  	*  @return Slow Stochastic Chart ready to be displayed.
  	*/
    SlowStoChart(TimeSeries series, int kTimeFrame, int dTimeFrame, int overSoldIndex, int overBoughtIndex)
    {
        ClosePriceIndicator closePrice                      = new ClosePriceIndicator(series);
        StochasticOscillatorKIndicator stochasticOscillK    = new StochasticOscillatorKIndicator(series, kTimeFrame);
        StochasticOscillatorDIndicator stochasticOscillD    = new StochasticOscillatorDIndicator(stochasticOscillK, dTimeFrame);
        StochasticOscillatorDIndicator stochasticOscillDD   = new StochasticOscillatorDIndicator(stochasticOscillD);
        
        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, stochasticOscillDD, stochasticOscillD.toString()));
        mainGraph.addSeries(buildTimeSeries(series, stochasticOscillD, stochasticOscillK.toString()));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("Slow Stochastic", "Date", "Indicator Value", mainGraph, secondaryGraph,
                overSoldIndex, overBoughtIndex);
    }

	/**
	 * 
	 * @param series           	TimeSeries                       
	 * @param closePrice       	ClosePriceIndicator - Close Price Indicator
	 * @param KstoIndicator   	StochasticOscillatorKIndicator - K indicator                 
	 * @param DstoIndicator     StochasticOscillatorDIndicator - D indicator                
	 * @param overSoldIndex     Int - Indication for 'OverSold'  
	 * @param overBoughtIndex   Int - indication for 'OverBought'
	 * @return Slow Stochastic Chart ready to be displayed.
	 */
	SlowStoChart(TimeSeries series, ClosePriceIndicator closePrice, StochasticOscillatorKIndicator KstoIndicator, StochasticOscillatorDIndicator DstoIndicator, int overSoldIndex, int overBoughtIndex)
    {

		StochasticOscillatorDIndicator stochasticOscillDD   = new StochasticOscillatorDIndicator(DstoIndicator);
        
        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, stochasticOscillDD, DstoIndicator.toString()));
        mainGraph.addSeries(buildTimeSeries(series, DstoIndicator, KstoIndicator.toString()));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("Slow Stochastic", "Date", "Indicator Value", mainGraph, secondaryGraph,
                overSoldIndex, overBoughtIndex);
    }

    
    //===============================
    
    
    public static void main(String[] args) {

        // Getting the time series
        //TimeSeries series = ta4jPack.LoadCSV();

        CsvLoader newLoader = new CsvHistoricalLoader("TLV","ilx" , new DateTime(2014 , Month.APRIL , 5 , 0 , 0), YahooHistCsvPath.YahooHistInterval.WEEKLY);
        TimeSeries series = newLoader.getSeries();

        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);
        
        ClosePriceIndicator closePrice                      = new ClosePriceIndicator(series);
        StochasticOscillatorKIndicator stochasticOscillK    = new StochasticOscillatorKIndicator(series, 14);
        StochasticOscillatorDIndicator stochasticOscillD    = new StochasticOscillatorDIndicator(stochasticOscillK, 3);

        Chart SlowStoChart1 = new SlowStoChart(series,14,3,20,80);
        SlowStoChart1.addBuySellSignals(series, strategy);
        Chart SlowStoChart2 = new SlowStoChart(series,closePrice,stochasticOscillK,stochasticOscillD,20,80);
        
        /**
         * Displaying the chart
         */
        displayChart("Stock Stats" , SlowStoChart1.getChart());
        displayChart("Stock Stats" , SlowStoChart2.getChart());
    }
}
