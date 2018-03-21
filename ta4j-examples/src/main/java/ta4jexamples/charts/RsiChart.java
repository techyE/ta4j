package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import ta4jexamples.loaders.CsvTradesLoader;

public class RsiChart extends AbstractChart
{

    /**
     *  @param series           TimeSeries.
     *  @param timeFrame        Indicator time frame.
     *  @param overSoldIndex    Indication for 'OverSold'.
     *  @param overBoughtIndex  Indication for 'OverBought'.
     *  @return RSI Oscillator Chart.
     */
    RsiChart(TimeSeries series, int timeFrame, int overSoldIndex, int overBoughtIndex)
    {
        ClosePriceIndicator closePrice  = new ClosePriceIndicator(series);
        RSIIndicator rsi                = new RSIIndicator(closePrice, timeFrame);

        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, rsi, rsi.toString(), timeFrame));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("RSI", "Date", "Indicator Value", mainGraph, secondaryGraph, overSoldIndex, overBoughtIndex);

    }

    /**
     *
     * @param series            TimeSeries.
     * @param closePrice        Close Price Indicator.
     * @param rsi               RSI Indicator.
     * @param overSoldIndex     Indication for 'OverSold'.
     * @param overBoughtIndex   Indication for 'OverBought'.
     * @return RSI Chart.
     */
    RsiChart(TimeSeries series, ClosePriceIndicator closePrice, RSIIndicator rsi,  int overSoldIndex, int overBoughtIndex)
    {

        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, rsi, rsi.toString(), rsi.getTimeFrame()));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("RSI", "Date", "Indicator Value", mainGraph, secondaryGraph, overSoldIndex, overBoughtIndex);
    }


    //===============================


    public static void main(String[] args)
    {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        Chart RsiChart1 = new RsiChart(series, 14, 30, 70);
        Chart RsiChart2 = new RsiChart(series, closePrice, rsi, 30, 70);

        /**
         * Displaying the chart
         */
        displayChart("Stock Stats", RsiChart1.getChart());
        displayChart("Stock Stats", RsiChart2.getChart());
    }
}

