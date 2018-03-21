/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 Marc de Verdelhan & respective authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eu.verdelhan.ta4j.indicators.trackers;

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;

/**
 * <p>
 * Name: Moving Average Convergence Divergence Indicator.       <br>
 * Type: Lagging.                                               <br>
 * Indicates when 2 averages crossing each other.               <br>
 * Used the stock prices difference by absolute stock prices.   <br>
 *<br>
 * When indicator > 0                       - bullish trend.    <br>
 * When indicator < 0                       - bearish trend.    <br>
 * When indicator cross up indicatorAvg     - up ticks.         <br>
 * When indicator cross down indicatorAvg   - down ticks.       <br>
 *
 * </p>
 */
public class MACDIndicator extends CachedIndicator<Decimal> {

    private final EMAIndicator shortTermEma;

    private final EMAIndicator longTermEma;

    /**
     *
     * @param indicator         Indicator on which MACD will be calculated
     * @param shortTimeFrame    EMA short time frame
     * @param longTimeFrame     EMA long time frame
     */
    public MACDIndicator(Indicator<Decimal> indicator, int shortTimeFrame, int longTimeFrame) {
        super(indicator);
        if (shortTimeFrame > longTimeFrame) {
            throw new IllegalArgumentException("Long term period count must be greater than short term period count");
        }
        shortTermEma = new EMAIndicator(indicator, shortTimeFrame);
        longTermEma = new EMAIndicator(indicator, longTimeFrame);
    }

    /**
     *
     * @param shortTermEma  EMAIndicator -  Short EMA indicator
     * @param longTermEma   EMAIndicator -  Long EMA indicator
     */
    public MACDIndicator(EMAIndicator shortTermEma, EMAIndicator longTermEma)
    {
        super(shortTermEma);    // Receives the time series in order to make calcs.
        if (shortTermEma.getTimeFrame() > longTermEma.getTimeFrame()) {
            throw new IllegalArgumentException("Long term period count must be greater than short term period count");
        }
        this.shortTermEma   = shortTermEma;
        this.longTermEma    = longTermEma;
    }

    @Override
    protected Decimal calculate(int index) {
        return shortTermEma.getValue(index).minus(longTermEma.getValue(index));
    }

    public int getLongTermEma() {
        return longTermEma.getTimeFrame();
    }

    public int getShortTermEma() {
        return shortTermEma.getTimeFrame();
    }
}