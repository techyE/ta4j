package ta4jexamples.charts;

import java.awt.Color;
import java.util.Date;

import eu.verdelhan.ta4j.*;
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

import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

public class CandleStickChart extends AbstractChart
{

    /**
     * Crate data for CandleStick chart with specific start and end.
     * @param series        TimeSeries
     * @param startTick     int - Tick to start with.
     * @param endTick       int - Tick to end with.
     */
    CandleStickChart(TimeSeries series, int startTick, int endTick)
    {
        
        TimeSeries subSeries                = series.subseries(startTick, endTick);
        
        // Creating the OHLC dataSet
        OHLCDataset ohlcDataset = createOHLCDataSet(subSeries);
        ClosePriceIndicator closePrice      = new ClosePriceIndicator(subSeries);
        
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(AbstractChart.buildTimeSeries(subSeries, closePrice, "Close Prices"));

        //Creating the chart
        JFreeChart chart = ChartFactory.createCandlestickChart(
                "CandleStick Chart",
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
        plot.setRenderer(renderer);
        
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
    CandleStickChart(TimeSeries series)
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
        Tick tick;

        Date[] dates        = new Date[nbTicks];
        double[] opens      = new double[nbTicks];
        double[] highs      = new double[nbTicks];
        double[] lows       = new double[nbTicks];
        double[] closes     = new double[nbTicks];
        double[] volumes    = new double[nbTicks];

        for (int i = BeginIndex ; i < nbTicks + BeginIndex; i++)
        {
            tick   = series.getTick(i);
            dates[i-BeginIndex]    = tick.getEndTime().toDate();
            opens[i-BeginIndex]    = tick.getOpenPrice().toDouble();
            highs[i-BeginIndex]    = tick.getMaxPrice().toDouble();
            lows[i-BeginIndex]     = tick.getMinPrice().toDouble();
            closes[i-BeginIndex]   = tick.getClosePrice().toDouble();
            volumes[i-BeginIndex]  = tick.getVolume().toDouble();
            }

        return new DefaultHighLowDataset("Candle View", dates, highs, lows, opens, closes, volumes);
    }


    //==================================


    public static void main(String[] args)
    {
        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);

        Chart CandleChart = new CandleStickChart(series, 30 , 100);
        CandleChart.addBuySellSignals(series, strategy);
        displayChart("Stock Stats", CandleChart.getChart());
        
    }
}
