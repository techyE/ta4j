package ta4jexamples.charts;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import eu.verdelhan.ta4j.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import static eu.verdelhan.ta4j.General.DEBUG_MODE;

/*
 * Useful function for chart building.
 */
public abstract class AbstractChart implements Chart
{
	
    final static Color stockValColor = new Color(90, 90, 90);

    //=======================================


    JFreeChart chart;
    @Override
    public JFreeChart getChart()
    {
        return chart;
    }

    //=======================================
    
	 /**
     * <p>
     * Builds a JFreeChart time series from a Ta4j time series and an indicator.
     * </p>
     * @param tickSeries    	TimeSeries.
     * @param indicator     	Indicator
     * @param name          	String    -The name of the chart time series
     * @param startTick     	int       - The first tick that will show on chart.
     * @return The JFreeChart time series
     */
    public static org.jfree.data.time.TimeSeries buildTimeSeries(TimeSeries tickSeries, Indicator<Decimal> indicator, String name , int startTick)
    {
        org.jfree.data.time.TimeSeries chartTimeSeries = new org.jfree.data.time.TimeSeries(name);
        int tickCount       = tickSeries.getTickCount();
        int firstTickIndex  = tickSeries.getBegin();
        
        // Checker that makes sure tick threshold is being used properly.
        if (startTick >= tickCount || startTick < 0)
            try {throw new Exception("Tick Count is smaller then the required threshold. " + tickCount + "-" + startTick);}
            catch (Exception e) {e.printStackTrace();}
        
        // Run on the ticks and building the chart by matching date to value.
        for (int i = startTick ; i < tickCount ; i++)
        {
            Tick tick = tickSeries.getTick(firstTickIndex + i);
            chartTimeSeries.add(new Minute(tick.getEndTime().toDate()), indicator.getValue(i).toDouble());
            // When using subSeries a bug can occur and you will find yourself dealing with this function.
            // Please check the Indicator class - most of the time the problem is there with the "index".
        }
        
        return chartTimeSeries;
    }
    
    /**
     * <p>
     * Builds a JFreeChart time series from a Ta4j time series and an indicator.
     * Starts from tick number 0.
     * </p>
     * @param tickSeries    	TimeSeries.
     * @param indicator     	Indicator
     * @param name          	String    -The name of the chart time series
     */
    public static org.jfree.data.time.TimeSeries buildTimeSeries(TimeSeries tickSeries, Indicator<Decimal> indicator, String name)
        {return buildTimeSeries(tickSeries, indicator, name, 0);}
    
    
    //=======================================


    /**
     * <p>
     * Displays a chart in a frame.
     * Arranging the Charts in order if there is more then one chart.
     * </p>
     * @param charts    The charts to be displayed
     * @param name     	Name of the frame.
     */
    public static void displayChart(String name , final JFreeChart... charts)
    {

        final List<ChartPanel> panel = new ArrayList<>();

        // Create chart panel for each chart.
        for (JFreeChart chart : charts)
            panel.add(new ChartPanel(chart));

        // Create a frame that will contain the chart panels, and set layout style.
        final JFrame frame = new JFrame(name);

        // Quit Program when closing chart only on Debug Mode.
        if (DEBUG_MODE) {frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);}
        
        // Layout by x and y coordinates.
        GridBagConstraints layOut = new GridBagConstraints();
        frame.setLayout(new GridBagLayout());
        
        // Make ability of frame to stretch
        layOut.fill = GridBagConstraints.BOTH;
        layOut.weightx  = 1.0;
        layOut.weighty  = 1.0;
        
        // For each chart, set abilities and add to frame.
        final int numOfCharts   = panel.size();
        for (int i=0 ; i<numOfCharts ; i++)
        {
            panel.get(i).setFillZoomRectangle(true);                
            panel.get(i).setMouseWheelEnabled(true);                
            panel.get(i).setHorizontalAxisTrace(true);
            panel.get(i).setVerticalAxisTrace(true);
            //panel.get(i).setPreferredSize(new Dimension(50, 60));

            // Setting the Layout (2 Columns unlimited rows).
            layOut.gridx = (i%2);
            layOut.gridy = (i/2);
          
            // When Last Chart Remains and number of charts is odd, stretch chart over 2 columns.
            if (numOfCharts-i == 1)
                layOut.gridwidth = 2;
          
            frame.getContentPane().add(panel.get(i) , layOut);
        }
      
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);

    }

    //=======================================

    /**
    * <p>
    * Adds Secondary Indicator & Axis to an existing chart.
    * </p>
    * @param plot      XYPlot                - Chart's Plot
    * @param dataSet   TimeSeriesCollection  - Secondary graph that being add to chart.
    * @param axisName  String                - Name of axis.
    * 
    */
    public static void addSecondaryAxis(XYPlot plot, TimeSeriesCollection dataSet, String axisName)
    {
        NumberAxis cashAxis = new NumberAxis(axisName);         
        cashAxis.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, cashAxis);
        plot.setDataset(1, dataSet);
        plot.mapDatasetToRangeAxis(1, 1);
        StandardXYItemRenderer newAxisRenderer = new StandardXYItemRenderer();   
        newAxisRenderer.setSeriesPaint(0, stockValColor);
        plot.setRenderer(1, newAxisRenderer);
    }
    
    //====================================
    
    /**
     * <p>
     * Adds indication for Entry points and Exit points on charts.
     * </p>
     * @param series    TimeSeries.
     * @param strategy  Strategy.
     */
    @Override
    public void addBuySellSignals(TimeSeries series, Strategy strategy)
    {
        // Running the strategy
        List<Trade> trades = series.run(strategy).getTrades();
        XYPlot plot = getChart().getXYPlot();

        // Adding markers to plot
        for (Trade trade : trades)
        {
            // Buy signal
            double buySignalTickTime = new Minute(series.getTick(trade.getEntry().getIndex()).getEndTime().toDate()).getFirstMillisecond();
            Marker buyMarker = new ValueMarker(buySignalTickTime);
            buyMarker.setPaint(Color.BLUE);
            buyMarker.setLabel("B");
            plot.addDomainMarker(buyMarker);
            
            // Sell signal, When Sell indicator is not artificial.
            if (!trade.getExit().getArtificial())
            {
                double sellSignalTickTime = new Minute(series.getTick(trade.getExit().getIndex()).getEndTime().toDate()).getFirstMillisecond();
                Marker sellMarker = new ValueMarker(sellSignalTickTime);
                sellMarker.setPaint(Color.BLACK);
                sellMarker.setLabel("S");
                plot.addDomainMarker(sellMarker);
            }

            // DEBUG! - Sell signal, When Sell indicator is artificial.
            if (DEBUG_MODE & trade.getExit().getArtificial())
            {
                double sellSignalTickTime = new Minute(series.getTick(trade.getExit().getIndex()).getEndTime().toDate()).getFirstMillisecond();
                Marker sellMarker = new ValueMarker(sellSignalTickTime);
                sellMarker.setPaint(Color.ORANGE);
                sellMarker.setLabel("A");
                plot.addDomainMarker(sellMarker);
            }

        }
        
    }
    
    //=============================================

    /**
     * <p>
     * Runs a strategy over a time series and adds the Markers Signal
     * corresponding to strategy EntryTrade/ExitTrade.
     * </p>
     * @param series        TimeSeries.
     * @param strategy      Strategy - the strategy that being monitored.
     */
    @Override
    public void addSatisfiedSignals(TimeSeries series, Strategy strategy)
    {
        XYPlot plot         = getChart().getXYPlot();
        int numOfTicks      = series.getTickCount();
        
        // Adding markers to plot according to number of ticks
        for (int i = 0 ; i < numOfTicks ; i++)
        {
            // When buying rule is satisfied, indicate it on the chart
            if (strategy.shouldEnter(i))
            {
                double satisfiedTick = new Minute(series.getTick(i).getEndTime().toDate()).getFirstMillisecond();
                Marker buyMarker = new ValueMarker(satisfiedTick);
                
                // When Buying signal is also a Selling signal, Color Magenta
                if (strategy.shouldExit(i))
                    buyMarker.setPaint(Color.MAGENTA);
                // When Only Buying Signal, Color Blue
                else
                    buyMarker.setPaint(Color.BLUE);
                
                plot.addDomainMarker(buyMarker);
            }
            // When Selling rule is satisfied, indicate it in red.
            else if (strategy.shouldExit(i))
            {
                double satisfiedTick = new Minute(series.getTick(i).getEndTime().toDate()).getFirstMillisecond();
                Marker buyMarker = new ValueMarker(satisfiedTick);
                buyMarker.setPaint(Color.RED);
                plot.addDomainMarker(buyMarker);
            }
        }
    }
    
    /**
     * <p>
     * Runs a strategy over a time series and marks signal satisfaction
     * according to the given rule.
     * </p>
     * @param series        TimeSeries.
     * @param rule     Rule - the rule the being monitored.
     */
    @Override
    public void addSatisfiedSignals(TimeSeries series, Rule rule)
    {
        XYPlot plot         = getChart().getXYPlot();
        Strategy strategy   = new Strategy(rule,rule);
        int numOfTicks      = series.getTickCount();
        
        // Adding markers to plot according to number of ticks
        for (int i = 0 ; i < numOfTicks ; i++)
        {
            // When buying rule is satisfied, indicate it on the chart
            if (strategy.shouldEnter(i))
            {
                double satisfiedTick = new Minute(series.getTick(i).getEndTime().toDate()).getFirstMillisecond();
                Marker buyMarker = new ValueMarker(satisfiedTick);
                buyMarker.setPaint(Color.BLUE);
                plot.addDomainMarker(buyMarker);
            }
        }
    }


    //=============================================================

    /**
     * <p>
     * Generate basic chart characteristics which are commonly used,
     * and easy to configure.
     * </p>
     *
     * @param chartName     Chart title.
     * @param xLabel        X label title.
     * @param yLabel        Y label title.
     * @param mainGraph     Main graph & Main Axis.
     * @return Basic chart configuration.
     */
    JFreeChart basicChart(String chartName, String xLabel, String yLabel, TimeSeriesCollection mainGraph)
    {
        JFreeChart chartChar = ChartFactory.createTimeSeriesChart(
                chartName,      // title
                xLabel,         // x-axis label
                yLabel,         // y-axis label
                mainGraph,      // data
                true,           // create legend?
                true,           // generate tooltips?
                false           // generate URLs?
        );
        XYPlot plot = (XYPlot) chartChar.getPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        // Edit Background.
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setBackgroundPaint(Color.white);
        // X axis by tick date.
        DateAxis axisX = (DateAxis) plot.getDomainAxis();
        axisX.setDateFormatOverride(new SimpleDateFormat("dd-MM-yy"));

        return chartChar;

    }

    /**
     * <p>
     * Generate basic chart characteristics which are commonly used,
     * and easy to configure.
     * </p>
     *
     * @param chartName     Chart title.
     * @param xLabel        X label title.
     * @param yLabel        Y label title.
     * @param mainGraph     Main graph & Main Axis.
     * @param yIndexes      Important indication lines over the graph.
     * @return Basic chart configuration.
     */
    JFreeChart basicChart(String chartName, String xLabel, String yLabel, TimeSeriesCollection mainGraph,
                          int... yIndexes)
    {
        JFreeChart chartChar = this.basicChart(chartName, xLabel, yLabel, mainGraph);
        XYPlot plot = (XYPlot) chartChar.getPlot();
        // Set margin lines.
        for (int yIndex : yIndexes)
        {
            Marker yIndexMarker = new ValueMarker(yIndex);
            yIndexMarker.setPaint(Color.BLACK);
            plot.addRangeMarker(yIndexMarker);
        }

        return chartChar;

    }

    /**
     * <p>
     * Generate basic chart characteristics which are commonly used,
     * and easy to configure.
     * </p>
     *
     * @param chartName         Chart title.
     * @param xLabel            X label title.
     * @param yLabel            Y label title.
     * @param mainGraph         Main graph & Main Axis.
     * @param secondaryGraph    Secondary graph & Secondary Axis.
     * @return Basic chart configuration.
     */
    JFreeChart basicChart(String chartName, String xLabel, String yLabel, TimeSeriesCollection mainGraph,
                          TimeSeriesCollection secondaryGraph)
    {
        JFreeChart chartChar = this.basicChart(chartName, xLabel, yLabel, mainGraph);
        XYPlot plot = (XYPlot) chartChar.getPlot();
        addSecondaryAxis(plot, secondaryGraph, "Stock Value");

        return chartChar;

    }

    /**
     * <p>
     * Generate basic chart characteristics which are commonly used,
     * and easy to configure.
     * </p>
     *
     * @param chartName     Chart title.
     * @param xLabel        X label title.
     * @param yLabel        Y label title.
     * @param mainGraph     Main graph & Main Axis
     * @param secondaryGraph    Secondary graph & Secondary Axis..
     * @param yIndexes      Important indication lines over the graph.
     * @return Basic chart configuration.
     */
    JFreeChart basicChart(String chartName, String xLabel, String yLabel,
                          TimeSeriesCollection mainGraph, TimeSeriesCollection secondaryGraph,
                          int... yIndexes)
    {
        JFreeChart chartChar = this.basicChart(chartName, xLabel, yLabel, mainGraph);
        XYPlot plot = (XYPlot) chartChar.getPlot();
        // Set margin lines.
        for (int yIndex : yIndexes)
        {
            Marker yIndexMarker = new ValueMarker(yIndex);
            yIndexMarker.setPaint(Color.BLACK);
            plot.addRangeMarker(yIndexMarker);
        }
        // Add secondary Axis
        addSecondaryAxis(plot, secondaryGraph, "Stock Value");

        return chartChar;

    }


    //===============================

}
