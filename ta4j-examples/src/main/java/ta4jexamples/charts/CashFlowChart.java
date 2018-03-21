package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.indicators.simple.ClosePercentageIndicator;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.TradingRecord;
import eu.verdelhan.ta4j.analysis.CashFlow;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class CashFlowChart extends AbstractChart
{

    /**
     * Create data about cash flow.
     * @param series        TimeSeries  -
     * @param strategy      Strategy    -
     * @param startTick     int         - Start point
     */
    CashFlowChart(TimeSeries series, Strategy strategy, int startTick)
    {
        
        TradingRecord tradingRecord             = series.run(strategy);
        CashFlow cashFlow                       = new CashFlow(series, tradingRecord);

        TimeSeries  subSeries                   = series.subseries(startTick, series.getEnd());
        ClosePercentageIndicator closePercent   = new ClosePercentageIndicator(subSeries);
        
        // Add Directional movements to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(subSeries, closePercent, "Buy & Hold"));
        mainGraph.addSeries(buildTimeSeries(series, cashFlow, "Cash Flow", startTick));

        chart = basicChart("Cash Flow Behavior", "Date", "Change %", mainGraph);
        
    }

    /**
     * Create data about cash flow.
     * @param series    TimeSeries
     * @param strategy  Strategy
     */
    CashFlowChart(TimeSeries series, Strategy strategy)
        {this(series,strategy,series.getBegin());}


    //=====================================================
    
    
	public static void main(String[] args)
    {

        final int startTick = 200;

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);
        strategy.setUnstablePeriod(startTick);
	    
	    Chart CashFlowChart = new CashFlowChart(series, strategy, startTick);
	    CashFlowChart.addBuySellSignals(series, strategy);
        // Displaying the chart
        displayChart("JoNes - Cash Flow" , CashFlowChart.getChart());
    }
	
}
