package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class ClosePriceChart extends AbstractChart
{

    /**
     * Create data about stock value.
     * @param series        TimeSeries
     */
    ClosePriceChart(TimeSeries series)
    {
        
        ClosePriceIndicator closePrice    = new ClosePriceIndicator(series);
        
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));

        chart = basicChart("Close Price Chart", "Date", "Stock Value", mainGraph);
        
    }


    //==============================================
    
    
    public static void main(String[] args)
    {
        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        Chart ClosePriceChart   = new ClosePriceChart(series);
        JFreeChart chart        = ClosePriceChart.getChart();
        ClosePriceChart.addBuySellSignals(series, strategy);
        ClosePriceChart.addSatisfiedSignals(series, strategy);

        // Display the Chart
        displayChart("JoNeS Buy & Sell" , chart);
    }
    
}
