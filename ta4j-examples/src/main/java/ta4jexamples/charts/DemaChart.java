package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.trackers.DoubleEMAIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class DemaChart extends AbstractChart
{

    /**
     * Create data about DEMA Indicator Chart.
     * @param series		TimeSeries - The Series of ticks
     * @param demaIndexes	DEmaIndicator Array - Wanted DEMA Indicators.
     * @return              DEMA Chart Ready to be displayed.
     */
    DemaChart(TimeSeries series, int... demaIndexes)
    {

        ClosePriceIndicator closePrice    = new ClosePriceIndicator(series);
        DoubleEMAIndicator DEMA;

        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));

        // When more than 1 EMA indicator is required, keep adding EMA indicators to chart.
        for (int demaIndex : demaIndexes)
        {
            DEMA = new DoubleEMAIndicator(closePrice, demaIndex);
            mainGraph.addSeries(buildTimeSeries(series, DEMA, DEMA.toString(), 2*demaIndex)); //2 for Double
        }

        chart = basicChart("DEMA Chart", "Date", "Stock Value", mainGraph);

    }

    /**
     * Create data about EMA Indicator Chart.
     * @param series		    TimeSeries - The Series of ticks
     * @param closePrice	    ClosePriceIndicator - close price reference indicator.
     * @param demaIndicators	DEMAIndicator Array - Wanted DEMA Indicators.
     * @return                  DEMA Chart Ready to be displayed.
     */
    DemaChart(TimeSeries series, ClosePriceIndicator closePrice, DoubleEMAIndicator... demaIndicators)
    {

        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));

        // When more than 1 EMA indicator is required, keep adding EMA indicators to chart.
        for (DoubleEMAIndicator demaIndicator : demaIndicators)
            mainGraph.addSeries(buildTimeSeries(series, demaIndicator, demaIndicator.toString(), 2*demaIndicator.getTimeFrame()));  // 2 for Double

        chart = basicChart("DEMA Chart", "Date", "Stock Value", mainGraph);

    }


    //===============================


    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        ClosePriceIndicator closePrice  = new ClosePriceIndicator(series);
        DoubleEMAIndicator DEMA         = new DoubleEMAIndicator(closePrice, 15);

        /**
         * Displaying the chart
         */
        int[] DEMAs = {5,10};//,30,40,50,60,70,80,90,100};
        Chart DemaChart1 = new DemaChart(series, DEMAs);
        Chart DemaChart2 = new DemaChart(series, closePrice, DEMA);
        DemaChart1.addBuySellSignals(series, strategy);
        DemaChart2.addBuySellSignals(series, strategy);

        displayChart("Stock Stats" , DemaChart1.getChart());
        displayChart("Stock Stats" , DemaChart2.getChart());

    }


}
