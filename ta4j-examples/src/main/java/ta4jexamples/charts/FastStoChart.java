package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorDIndicator;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticOscillatorKIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import ta4jexamples.loaders.CsvTradesLoader;

public class FastStoChart extends AbstractChart
{

    /**
    *  @param series TimeSeries
  	*  @param kTimeFrame Int - K indicator
  	*  @param dTimeFrame Int - D indicator
  	*  @param overSoldIndex Int - Indication for 'OverSold'
  	*  @param overBoughtIndex Int - indication for 'OverBought'
	*  @return Fast Stochastic Chart ready to be displayed.
  	*/
	FastStoChart(TimeSeries series, int kTimeFrame, int dTimeFrame, int overSoldIndex, int overBoughtIndex)
    {
        ClosePriceIndicator closePrice                      = new ClosePriceIndicator(series);
        StochasticOscillatorKIndicator stochasticOscillK    = new StochasticOscillatorKIndicator(series, kTimeFrame);
        StochasticOscillatorDIndicator stochasticOscillD    = new StochasticOscillatorDIndicator(stochasticOscillK, dTimeFrame);

        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, stochasticOscillD, stochasticOscillD.toString()));
        mainGraph.addSeries(buildTimeSeries(series, stochasticOscillK, stochasticOscillK.toString()));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("Fast Stochastic", "Date", "Indicator Value", mainGraph, secondaryGraph,
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
	 * @return Fast Stochastic Chart ready to be displayed.
	 */
	FastStoChart(TimeSeries series, ClosePriceIndicator closePrice, StochasticOscillatorKIndicator KstoIndicator, StochasticOscillatorDIndicator DstoIndicator, int overSoldIndex, int overBoughtIndex)
    {
        
        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, DstoIndicator, DstoIndicator.toString()));
        mainGraph.addSeries(buildTimeSeries(series, KstoIndicator, KstoIndicator.toString()));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("Fast Stochastic", "Date", "Indicator Value", mainGraph, secondaryGraph,
                overSoldIndex, overBoughtIndex);
    }

	
    //===============================
    
    
    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");



        ClosePriceIndicator closePrice                      = new ClosePriceIndicator(series);
        StochasticOscillatorKIndicator stochasticOscillK    = new StochasticOscillatorKIndicator(series, 14);
        StochasticOscillatorDIndicator stochasticOscillD    = new StochasticOscillatorDIndicator(stochasticOscillK, 3);
        
        Chart FastStoChart1 = new FastStoChart(series,14,3,20,80);
        Chart FastStoChart2 = new FastStoChart(series,closePrice,stochasticOscillK,stochasticOscillD,20,80);
        
        /**
         * Displaying the chart
         */
        displayChart("Stock Stats" , FastStoChart1.getChart());
        displayChart("Stock Stats" , FastStoChart2.getChart());
    }

}
