package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.data.time.TimeSeriesCollection;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.TripleEMAIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class TemaChart extends AbstractChart
{

    /**
     * Create data about EMA Indicator Chart.
     * @param series		TimeSeries - The Series of ticks
     * @param temaIndexes	EmaIndicator Array - Wanted EMA Indicators.
     * @return              EMA Chart Ready to be displayed.
     */
    TemaChart(TimeSeries series, int... temaIndexes)
    {

        ClosePriceIndicator closePrice    = new ClosePriceIndicator(series);
        TripleEMAIndicator TEMA;

        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));

        // When more than 1 EMA indicator is required, keep adding EMA indicators to chart.
        for (int temaIndex : temaIndexes)
        {
            TEMA = new TripleEMAIndicator(closePrice, temaIndex);
            mainGraph.addSeries(buildTimeSeries(series, TEMA, TEMA.toString(), 3*temaIndex)); // 3 for Triple.
        }

        chart = basicChart("TEMA Chart", "Date", "Stock Value", mainGraph);

    }

    /**
     * Create data about EMA Indicator Chart.
     * @param series		    TimeSeries - The Series of ticks
     * @param closePrice	    ClosePriceIndicator - close price reference indicator.
     * @param temaIndicators	TEMA Array - Wanted TEMA Indicators.
     * @return                  TEMA Chart Ready to be displayed.
     */
    TemaChart(TimeSeries series, ClosePriceIndicator closePrice, TripleEMAIndicator... temaIndicators)
    {

        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Close Prices"));

        // When more than 1 EMA indicator is required, keep adding EMA indicators to chart.
        for (TripleEMAIndicator temaIndicator : temaIndicators)
            mainGraph.addSeries(buildTimeSeries(series, temaIndicator, temaIndicator.toString(), 3*temaIndicator.getTimeFrame())); // 3 for Triple.

        chart = basicChart("TEMA Chart", "Date", "Stock Value", mainGraph);

    }


    //===============================


    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        ClosePriceIndicator closePrice  = new ClosePriceIndicator(series);
        TripleEMAIndicator TEMA         = new TripleEMAIndicator(closePrice, 15);

        /**
         * Displaying the chart
         */
        int[] TEMAs = {10,20};//,30,40,50,60,70,80,90,100};
        Chart TemaChart1 = new TemaChart(series, TEMAs);
        Chart TemaChart2 = new TemaChart(series, closePrice, TEMA);
        TemaChart1.addBuySellSignals(series, strategy);
        TemaChart2.addBuySellSignals(series, strategy);

        displayChart("Stock Stats" , TemaChart1.getChart());
        displayChart("Stock Stats" , TemaChart2.getChart());

    }


}
