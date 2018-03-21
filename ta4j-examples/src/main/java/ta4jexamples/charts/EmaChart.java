package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.data.time.TimeSeriesCollection;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.EMAIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class EmaChart extends AbstractChart
{

    /**
	 * Create data about EMA Indicator Chart.
	 * @param series		TimeSeries - The Series of ticks
	 * @param emaIndexes	EmaIndicator Array - Wanted EMA Indicators.
	 * @return              EMA Chart Ready to be displayed.
	 */
    EmaChart(TimeSeries series, int... emaIndexes)
	{
		
		ClosePriceIndicator closePrice    = new ClosePriceIndicator(series);
		EMAIndicator EMA;
		
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));
		
        // When more than 1 EMA indicator is required, keep adding EMA indicators to chart.
        for (int emaIndex : emaIndexes)
        {
            EMA = new EMAIndicator(closePrice, emaIndex);
            mainGraph.addSeries(buildTimeSeries(series, EMA, EMA.toString(), emaIndex));
        }

        chart = basicChart("EMA Chart", "Date", "Stock Value", mainGraph);
		
	}
	
	/**
     * Create data about EMA Indicator Chart.
	 * @param series		TimeSeries - The Series of ticks
	 * @param closePrice	ClosePriceIndicator - close price reference indicator.
	 * @param emaIndicators	EMAIndicator Array - Wanted EMA Indicators.
	 * @return              EMA Chart Ready to be displayed.
	 */
    EmaChart(TimeSeries series, ClosePriceIndicator closePrice, EMAIndicator... emaIndicators)
	{
		
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));
		
        // When more than 1 EMA indicator is required, keep adding EMA indicators to chart.
        for (EMAIndicator emaIndicator : emaIndicators)
            mainGraph.addSeries(buildTimeSeries(series, emaIndicator, emaIndicator.toString(), emaIndicator.getTimeFrame()));

        chart = basicChart("EMA Chart", "Date", "Stock Value", mainGraph);
		
	}
    
	
    //===============================
    
    
    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        ClosePriceIndicator closePrice    = new ClosePriceIndicator(series);
        EMAIndicator EMA = new EMAIndicator(closePrice, 15);
       
        /**
         * Displaying the chart
         */
        int[] EMAs = {4,8};//,30,40,50,60,70,80,90,100};
        Chart EmaChart1 = new EmaChart(series, EMAs);
        Chart EmaChart2 = new EmaChart(series, closePrice, EMA);
        EmaChart1.addBuySellSignals(series, strategy);
        EmaChart2.addBuySellSignals(series, strategy);
        
        displayChart("Stock Stats" , EmaChart1.getChart());
        displayChart("Stock Stats" , EmaChart2.getChart());

    }

    
}
