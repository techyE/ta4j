package ta4jexamples.charts;

import eu.verdelhan.ta4j.Chart;
import eu.verdelhan.ta4j.General;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsLowerIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsMiddleIndicator;
import eu.verdelhan.ta4j.indicators.trackers.bollinger.BollingerBandsUpperIndicator;
import org.jfree.data.time.TimeSeriesCollection;
import ta4jexamples.loaders.CsvTradesLoader;


public class BollingerChart extends AbstractChart
{

    BollingerChart(TimeSeries series)
    {
        ClosePriceIndicator closePrice              = new ClosePriceIndicator(series);
        BollingerBandsMiddleIndicator middleBBand   = new BollingerBandsMiddleIndicator(closePrice);
        BollingerBandsLowerIndicator lowBBand       = new BollingerBandsLowerIndicator(middleBBand, closePrice);
        BollingerBandsUpperIndicator upBBand        = new BollingerBandsUpperIndicator(middleBBand, closePrice);

        // Building the main graph
        TimeSeriesCollection mainGraph = new TimeSeriesCollection();
        mainGraph.addSeries(buildTimeSeries(series, closePrice, "Middle Bollinger Band"));
        mainGraph.addSeries(buildTimeSeries(series, lowBBand, "Low Bollinger Band"));
        mainGraph.addSeries(buildTimeSeries(series, upBBand, "High Bollinger Band"));
        // Add Close Price to chart.
        TimeSeriesCollection secondaryGraph = new TimeSeriesCollection();
        secondaryGraph.addSeries(buildTimeSeries(series, closePrice, "Close Price"));


        chart = basicChart("Bollinger Lines Chart", "Date", "Standard Deviation", mainGraph, secondaryGraph);
        
    }


    //=============================================
    
    
    public static void main(String[] args)
    {

        // Getting the time series
        final TimeSeries series = CsvTradesLoader.loadBitstampSeries("bitstamp_trades_from_20131125_usd.csv");

        Chart bollingChart = new BollingerChart(series);
        
        displayChart("Stock Stats" , bollingChart.getChart());
        
    }
    
}
