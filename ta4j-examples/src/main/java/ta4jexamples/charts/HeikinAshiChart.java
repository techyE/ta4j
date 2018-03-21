package ta4jexamples.charts;

import eu.verdelhan.ta4j.*;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

import java.awt.*;
import java.util.Date;

public class HeikinAshiChart extends AbstractChart
{

    /**
     * Crate data for CandleStick chart with specific start and end.
     * @param series        TimeSeries
     * @param startTick     int - Tick to start with.
     * @param endTick       int - Tick to end with.
     */
    HeikinAshiChart(TimeSeries series, int startTick, int endTick)
    {

        TimeSeries subSeries                = series.subseries(startTick, endTick);

        // Creating the OHLC dataSet
        OHLCDataset ohlcDataset = createOHLCDataSet(subSeries);
        ClosePriceIndicator closePrice      = new ClosePriceIndicator(subSeries);

        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(AbstractChart.buildTimeSeries(subSeries, closePrice, "Close Prices"));

        //Creating the chart
        JFreeChart chart = ChartFactory.createCandlestickChart(
                "Heikin Ashi Chart",
                "Time",
                "Stock Value",
                ohlcDataset,
                true);
        XYPlot plot = chart.getXYPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        // Edit Background.
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);

        // Candlestick rendering - Keeping the candles in the same width.
        CandlestickRenderer renderer = new CandlestickRenderer();
        renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
        renderer.setAutoWidthGap(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
        renderer.setAutoWidthFactor(CandlestickRenderer.WIDTHMETHOD_SMALLEST);

        plot.setRenderer(renderer);
        //renderer.set

        // Additional dataSet
        int index = 1;
        plot.setDataset(index, mainGraph);
        plot.mapDatasetToRangeAxis(index, 0);

        // Connect between Candle close price and close indicator
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(index, Color.blue);
        plot.setRenderer(index, renderer2);

        NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setAutoRangeIncludesZero(false);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        this.chart = chart;
    }

    /**
     *
     * Create data for CandleStick chart on the whole series.
     * @param series    TimeSeries
     */
    HeikinAshiChart(TimeSeries series)
    {this(series, series.getBegin(), series.getEnd());}

    /**
     * Builds a JFreeChart OHLC dataSet from a ta4j time series.
     * @param series a time series
     * @return an Open-High-Low-Close dataSet
     */

    private static OHLCDataset createOHLCDataSet(TimeSeries series)
    {
        final int nbTicks = series.getTickCount();
        final int BeginIndex = series.getBegin();

        Date[] dates        = new Date[nbTicks];
        double[] opens      = new double[nbTicks];
        double[] highs      = new double[nbTicks];
        double[] lows       = new double[nbTicks];
        double[] closes     = new double[nbTicks];
        double[] volumes    = new double[nbTicks];

        Tick tick;
        Decimal CloseHA = null;
        Decimal oldCloseHA;
        Decimal OpenHA = null;
        Decimal oldOpenHA;

        for (int i = BeginIndex ; i < nbTicks + BeginIndex; i++)
        {

            tick                    = series.getTick(i);
            dates[i - BeginIndex]   = tick.getEndTime().toDate();
            volumes[i - BeginIndex] = tick.getVolume().toDouble();

            oldCloseHA        = CloseHA;
            CloseHA           = tick.getOpenPrice().plus(tick.getClosePrice())
                                .plus(tick.getMaxPrice()).plus(tick.getMinPrice())
                                .dividedBy(Decimal.valueOf(4));
            closes[i - BeginIndex]  = CloseHA.toDouble();

            if (i == BeginIndex) {
                OpenHA                  = tick.getOpenPrice();
                opens[i - BeginIndex]   = OpenHA.toDouble();
                highs[i - BeginIndex]   = tick.getMaxPrice().toDouble();
                lows[i - BeginIndex]    = tick.getMinPrice().toDouble();
            }
            else {
                oldOpenHA               = OpenHA;
                OpenHA                  = oldOpenHA.plus(oldCloseHA).dividedBy(Decimal.TWO);
                opens[i - BeginIndex]   = OpenHA.toDouble();
                highs[i - BeginIndex]   = tick.getMaxPrice().max(CloseHA).max(OpenHA).toDouble();
                lows[i - BeginIndex]    = tick.getMinPrice().min(CloseHA).min(OpenHA).toDouble();
            }

        }

        return new DefaultHighLowDataset("Heikin-Ashi Candle View", dates, highs, lows, opens, closes, volumes);
    }


    //==================================


    public static void main(String[] args)
    {
        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);


        Chart CandleChart = new HeikinAshiChart(series);
        CandleChart.addBuySellSignals(series, strategy);
        displayChart("Stock Stats", CandleChart.getChart());

    }
}
