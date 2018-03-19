package eu.verdelhan.ta4j.indicators.trackers;


import org.junit.Before;
import org.junit.Test;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;

import static eu.verdelhan.ta4j.TATestsUtils.assertDecimalEquals;

public class WilderSmoothIndicatorTest {

    private TimeSeries fakeSeries;
    private WilderSmoothIndicator wilderIndicator;
    int timeFrame;

    @Before
    public void setUp() {

//        fakeSeries                      = new MockTimeSeries(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18);
        fakeSeries                      = new MockTimeSeries(30);
        ClosePriceIndicator closePrice  = new ClosePriceIndicator(fakeSeries);
        timeFrame                       = 5;
        wilderIndicator                 = new WilderSmoothIndicator(closePrice, timeFrame);
    }
    
    @Test
    public void test()
    {

        int timeFrameMinusOne   = timeFrame-1;
        double wilderVal        = 0d;

        for (int i = 0 ; i<fakeSeries.getTickCount() ; i++)
        {
            double currentVal = fakeSeries.getTick(i).getClosePrice().toDouble();
            if (i==0)
                wilderVal = currentVal;
            else
                wilderVal = (wilderVal * timeFrameMinusOne + currentVal) / timeFrame;
            assertDecimalEquals(wilderIndicator.getValue(i), wilderVal);
        }
    }

}
