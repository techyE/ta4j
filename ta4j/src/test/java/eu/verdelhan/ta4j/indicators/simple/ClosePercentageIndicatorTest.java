package eu.verdelhan.ta4j.indicators.simple;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by yony on 3/19/18.
 */
public class ClosePercentageIndicatorTest {
    private ClosePercentageIndicator closepercetage;

    TimeSeries timeSeries;

    @Before
    public void setUp() {
        timeSeries = new MockTimeSeries(30);
        closepercetage = new ClosePercentageIndicator(timeSeries);
    }

    @Test
    public void indicatorShouldRetrieveTickClosePrice() {
        for (int i = 0; i < timeSeries.getTickCount() ; i++) {
            assertEquals(closepercetage.getValue(i), timeSeries.getTick(i).getClosePrice().dividedBy(timeSeries.getTick(0).getClosePrice()));
        }
    }
}
