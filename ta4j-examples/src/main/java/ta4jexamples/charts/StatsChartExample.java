package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import org.jfree.chart.JFreeChart;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class StatsChartExample
{
    
    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        Chart ATR = new AtrChart(series, 14);
        Chart CashFlow = new CashFlowChart(series, strategy, 200);
        Chart ADX = new AdxChart(series, 14, 20);
        Chart ClosePrice = new CandleStickChart(series);
        ClosePrice.addBuySellSignals(series, strategy);
        
        /**
         * Displaying the chart
         */
        JFreeChart[] charts = {ATR.getChart(), CashFlow.getChart(), ClosePrice.getChart(), ADX.getChart()};
        AbstractChart.displayChart("Stock Stats" , charts);
    }
	
}
