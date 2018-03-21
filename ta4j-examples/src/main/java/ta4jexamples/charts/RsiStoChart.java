package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.oscillators.StochasticRSIIndicator;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import ta4jexamples.loaders.CsvTradesLoader;

public class RsiStoChart extends AbstractChart
{

    /**
     *  @param series           TimeSeries.
     *  @param timeFrame        int - time frame.
     *  @param overSoldIndex    int - Indication for 'OverSold'.
     *  @param overBoughtIndex  int - Indication for 'OverBought'.
     *  @return RSI Stochastic Chart.
     */
    RsiStoChart(TimeSeries series, int timeFrame, int overSoldIndex, int overBoughtIndex)
    {
        ClosePriceIndicator closePrice          = new ClosePriceIndicator(series);
        RSIIndicator rsi                        = new RSIIndicator(closePrice, timeFrame);
        StochasticRSIIndicator rsiOsc = new StochasticRSIIndicator(rsi, timeFrame);

        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, rsiOsc, rsiOsc.toString(), timeFrame));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("RSI Stochastic", "Date", "Indicator Value", mainGraph, secondaryGraph, overSoldIndex, overBoughtIndex);

    }

    /**
     *
     * @param series            TimeSeries.
     * @param closePrice        Close Price Indicator.
     * @param rsiOsc            RSI Oscillator Indicator.
     * @param overSoldIndex     Indication for 'OverSold'.
     * @param overBoughtIndex   Indication for 'OverBought'.
     * @return RSI Stochastic Chart.
     */
    RsiStoChart(TimeSeries series, ClosePriceIndicator closePrice, StochasticRSIIndicator rsiOsc, int overSoldIndex, int overBoughtIndex)
    {

        // Add Stochastic to chart.
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, rsiOsc, rsiOsc.toString(), rsiOsc.getTimeFrame()));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        chart = basicChart("RSI Stochastic", "Date", "Indicator Value", mainGraph, secondaryGraph, overSoldIndex, overBoughtIndex);
    }


    //===============================


    public static void main(String[] args)
    {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");


        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);
        StochasticRSIIndicator rsiOsc = new StochasticRSIIndicator(rsi, 14);

        Chart RsiOscChart1 = new RsiStoChart(series, 14, 20, 80);
        Chart RsiOscChart2 = new RsiStoChart(series, closePrice, rsiOsc, 20, 80);

        /**
         * Displaying the chart
         */
        displayChart("Stock Stats", RsiOscChart1.getChart());
        displayChart("Stock Stats", RsiOscChart2.getChart());
    }
}