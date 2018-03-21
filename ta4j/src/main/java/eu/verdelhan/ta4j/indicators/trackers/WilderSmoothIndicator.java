package eu.verdelhan.ta4j.indicators.trackers;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 *<p>
 * Type: Lagging <br>
 * Overlay: V.   <br>
 * Smoothing Method that was developed by Welles Wilder.<br>
 * Similar to EMA but holds values backwards.<br>
 * Used mostly in "Wilder's Indicators" like ATR and ADX (Average Direction Movements).
 * </p>
 *
 */
public class WilderSmoothIndicator  extends CachedIndicator<Decimal> {

    private int timeFrame;
    private Indicator<Decimal> indicator;

    public WilderSmoothIndicator(Indicator<Decimal> indicator , int timeFrame)
    {
        super(indicator);
        this.indicator = indicator;
        this.timeFrame = timeFrame;
    }

    @Override
    protected Decimal calculate(int index)
    {
        if (index == 0) {
            return indicator.getValue(index);
        }
        Decimal nbPeriods           = Decimal.valueOf(timeFrame);
        Decimal nbPeriodsMinusOne   = Decimal.valueOf(timeFrame - 1);
        return getValue(index-1).multipliedBy(nbPeriodsMinusOne).plus(indicator.getValue(index)).dividedBy(nbPeriods);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + timeFrame;
    }
    
    public int getTimeFrame()
    {
    	return timeFrame;
    }
    
}
