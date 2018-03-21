package eu.verdelhan.ta4j.indicators.oscillators;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.helpers.HighestValueIndicator;
import eu.verdelhan.ta4j.indicators.helpers.LowestValueIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;


/**
 *<p>
 * Name: StochRSI               <br>
 * Type: Leading Indicator.     <br>
 * Oscillator Indicator, indicates when a stock is OverSold or OverBought.<br>
 * Checks how close is the Tick's RSI value to the highest RSI value in the time frame.<br><b>
 * 50 is the middle range, for buying or selling indication.<br>
 * Buy Above 50, Sell when Goes down of 80, (for Short under 50, Over 20).<br></b>
 * Might need the help of SMA in order to be less sensitive.<br>
 * Pros:<br>
 *     1. More sensitive then regular RSI indicator.<br>
 * Cons:<br>
 *     1. Too sensitive to changes.<br>
 *</p>
 */
public class StochasticRSIIndicator extends CachedIndicator<Decimal>
{

    private final RSIIndicator          rsi;
    private final int                   timeFrame;
    private HighestValueIndicator       highestRsiIndicator;
    private LowestValueIndicator        lowestRsiIndicator;

    /**
     * @param series    TimeSeries
     * @param timeFrame int - Number of ticks on which time frame will be calculated
     */
    public StochasticRSIIndicator(TimeSeries series, int timeFrame) {
        super(series);
        this.rsi                    = new RSIIndicator(series, timeFrame);
        this.timeFrame              = timeFrame;
        this.highestRsiIndicator    = new HighestValueIndicator(rsi , timeFrame);
        this.lowestRsiIndicator     = new LowestValueIndicator(rsi , timeFrame);
    }

    /**
     *
     * @param rsi       RSIIndicator - RSI Indicator on which StochRSI will be calculated
     * @param timeFrame int - Number of ticks on which time frame will be calculated
     */
    public StochasticRSIIndicator(RSIIndicator rsi, int timeFrame) {
        super(rsi);
        this.rsi                    = rsi;
        this.timeFrame              = timeFrame;
        this.highestRsiIndicator    = new HighestValueIndicator(rsi , timeFrame);
        this.lowestRsiIndicator     = new LowestValueIndicator(rsi , timeFrame);
    }


    @Override
    protected Decimal calculate(int index) {
        Decimal currRsi   = rsi.getValue(index);
        Decimal rsiHigh   = highestRsiIndicator.getValue(index);
        Decimal rsiLow    = lowestRsiIndicator.getValue(index);

        return currRsi.minus(rsiLow)
                .dividedBy(rsiHigh.minus(rsiLow))
                .multipliedBy(Decimal.HUNDRED);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " timeFrame: " + timeFrame;
    }

    public int getTimeFrame() {
        return timeFrame;
    }

}