package ta4jexamples.charts;

import java.awt.Color;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeriesCollection;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.helpers.DirectionalDownIndicator;
import eu.verdelhan.ta4j.indicators.helpers.DirectionalUpIndicator;
import eu.verdelhan.ta4j.indicators.trackers.DirectionalMovementIndicator;
import eu.verdelhan.ta4j.indicators.trackers.WilderSmoothIndicator;
import ta4jexamples.loaders.CsvTradesLoader;
import ta4jexamples.strategies.MovingMomentumStrategy;

/**
 * 
 * Average Directional Index Chart.
 * including Directional movement up & Down.
 *
 */
public class AdxChart extends AbstractChart
{
    
    /**
    *  @param series                TimeSeries.
    *  @param closePrice            The average value by Wilder Smoothing Technique.
    *  @param trendIndicationIndex  Margin line that indicates for when trend is starting.
    */
    AdxChart(TimeSeries series, ClosePriceIndicator closePrice, DirectionalMovementIndicator dix, DirectionalUpIndicator diu, DirectionalDownIndicator did, int trendIndicationIndex)
    {
        
        int calculatedAvg           = dix.getTimeFrame();
        WilderSmoothIndicator adx   = new WilderSmoothIndicator(dix, calculatedAvg);
        
        // Add Directional movements to chart..
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, adx, dix.toString(), calculatedAvg));
        mainGraph.addSeries(buildTimeSeries(series, diu, diu.toString(), calculatedAvg));
        mainGraph.addSeries(buildTimeSeries(series, did, did.toString(), calculatedAvg));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));

        JFreeChart chartFormat = basicChart("ADX Chart", "Date", "Stock Value", mainGraph, secondaryGraph, trendIndicationIndex);
        XYPlot plot     = (XYPlot) chartFormat.getPlot();
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(139,69,19));
        renderer.setSeriesPaint(1, new Color(90, 255, 90));
        renderer.setSeriesPaint(2, new Color(255, 90, 90));

        this.chart = chartFormat;
    }
    
    /**
    *  @param series                TimeSeries.
  	*  @param wilderAvgIndex        The average value by Wilder Smoothing Technique.
  	*  @param trendIndicationIndex  Margin line that indicates for when trend is starting.
  	*/
	AdxChart(TimeSeries series, int wilderAvgIndex, int trendIndicationIndex)
    {
        
        this(   series, 
                new ClosePriceIndicator(series),
                new DirectionalMovementIndicator(series, wilderAvgIndex),
                new DirectionalUpIndicator(series, wilderAvgIndex),
                new DirectionalDownIndicator(series, wilderAvgIndex),
                trendIndicationIndex);
    }
		
    //===============================
    
    
    public static void main(String[] args) {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");
        // Building the trading strategy
        final Strategy strategy = MovingMomentumStrategy.buildStrategy(series);
        
        ClosePriceIndicator closePrice      = new ClosePriceIndicator(series);
        
        DirectionalDownIndicator didown     = new DirectionalDownIndicator(series, 14);
        DirectionalUpIndicator diup         = new DirectionalUpIndicator(series, 14);
        DirectionalMovementIndicator dix    = new DirectionalMovementIndicator(series, 14);

        // Thread Example.
        final Chart chart1 = new AdxChart(series, 14, 20);
        chart1.addBuySellSignals(series, strategy);

        final Chart chart2 = new AdxChart(series, closePrice, dix,diup,didown,20);


        Thread thread1 = new Thread () {
            public void run () {
                displayChart("Stock Stats" , chart1.getChart());
            }
        };
        Thread thread2 = new Thread () {
            public void run () {
                displayChart("Stock Stats!!" , chart2.getChart());
            }
        };
        thread2.start();
        thread1.start();

    }

}
