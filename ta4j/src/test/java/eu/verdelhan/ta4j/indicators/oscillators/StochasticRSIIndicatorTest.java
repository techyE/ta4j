package eu.verdelhan.ta4j.indicators.oscillators;


import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.helpers.HighestValueIndicator;
import eu.verdelhan.ta4j.indicators.helpers.LowestValueIndicator;
import eu.verdelhan.ta4j.indicators.trackers.RSIIndicator;
import eu.verdelhan.ta4j.mocks.MockTick;
import eu.verdelhan.ta4j.mocks.MockTimeSeries;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static eu.verdelhan.ta4j.TATestsUtils.assertDecimalEquals;

public class StochasticRSIIndicatorTest {

    private TimeSeries data;
    private RSIIndicator rsi;
    private HighestValueIndicator highVal;
    private LowestValueIndicator lowVal;

    @Before
    public void setUp() {

        data        = new MockTimeSeries(40);
        rsi         = new RSIIndicator(data, 14);
        highVal     = new HighestValueIndicator(rsi, 14);
        lowVal      = new LowestValueIndicator(rsi, 14);
    }

    @Test
    public void StochasticRSIIndicatorAv14TimeSeries() {

        StochasticRSIIndicator stochRsi = new StochasticRSIIndicator(data, 14);

        assertDecimalEquals(stochRsi.getValue(0), 0);
        assertDecimalEquals(stochRsi.getValue(12), ((rsi.getValue(12).toDouble()-lowVal.getValue(12).toDouble())/(highVal.getValue(12).toDouble()-lowVal.getValue(12).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(13), ((rsi.getValue(13).toDouble()-lowVal.getValue(13).toDouble())/(highVal.getValue(13).toDouble()-lowVal.getValue(13).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(16), ((rsi.getValue(16).toDouble()-lowVal.getValue(16).toDouble())/(highVal.getValue(16).toDouble()-lowVal.getValue(16).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(20), ((rsi.getValue(20).toDouble()-lowVal.getValue(20).toDouble())/(highVal.getValue(20).toDouble()-lowVal.getValue(20).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(21), ((rsi.getValue(21).toDouble()-lowVal.getValue(21).toDouble())/(highVal.getValue(21).toDouble()-lowVal.getValue(21).toDouble()))*100);
    }

    @Test
    public void StochasticRSIIndicatorAv14RSI() {

        StochasticRSIIndicator stochRsi = new StochasticRSIIndicator(rsi, 14);

        assertDecimalEquals(stochRsi.getValue(0), 0);
        assertDecimalEquals(stochRsi.getValue(12), ((rsi.getValue(12).toDouble()-lowVal.getValue(12).toDouble())/(highVal.getValue(12).toDouble()-lowVal.getValue(12).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(13), ((rsi.getValue(13).toDouble()-lowVal.getValue(13).toDouble())/(highVal.getValue(13).toDouble()-lowVal.getValue(13).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(16), ((rsi.getValue(16).toDouble()-lowVal.getValue(16).toDouble())/(highVal.getValue(16).toDouble()-lowVal.getValue(16).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(20), ((rsi.getValue(20).toDouble()-lowVal.getValue(20).toDouble())/(highVal.getValue(20).toDouble()-lowVal.getValue(20).toDouble()))*100);
        assertDecimalEquals(stochRsi.getValue(21), ((rsi.getValue(21).toDouble()-lowVal.getValue(21).toDouble())/(highVal.getValue(21).toDouble()-lowVal.getValue(21).toDouble()))*100);
    }
}
