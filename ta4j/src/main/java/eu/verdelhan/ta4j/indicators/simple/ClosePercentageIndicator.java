package eu.verdelhan.ta4j.indicators.simple;

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 * <p>
 * Close price indicator, by percentage.
 * </p>
 */
public class ClosePercentageIndicator extends CachedIndicator<Decimal> {

    private TimeSeries series;
    private int firstTickIndex;

    public ClosePercentageIndicator(TimeSeries series)
    {
        super(series);
        this.series     	= series;
        this.firstTickIndex = series.getBegin();
    }
    
    @Override
    protected Decimal calculate(int index)
    {
        Decimal CurrPrice   = series.getTick(firstTickIndex+index).getClosePrice();
        Decimal StartPoint  = series.getTick(firstTickIndex).getClosePrice();
        return CurrPrice.dividedBy(StartPoint);
    }
}