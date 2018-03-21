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
package eu.verdelhan.ta4j.indicators.oscillators;

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;

/**
 * Stochastic oscillator D.
 * <p>
 * Receive {@link StochasticOscillatorKIndicator} and returns its {@link SMAIndicator SMAIndicator(3)}.
 */
public class StochasticOscillatorDIndicator extends CachedIndicator<Decimal> {

    private SMAIndicator sma;

    /**
     * Returns SMA value of given K indicator and timeFrame
     * @param k         Stochastic Oscillator K value
     * @param timeFrame Time frame on which average will be calculated
     */
    public StochasticOscillatorDIndicator(StochasticOscillatorKIndicator k, int timeFrame) {
        super(k);
        sma = new SMAIndicator(k, timeFrame);
    }

    /**
     * Returns SMA value of given K indicator and timeFrame
     * @param k         Stochastic Oscillator K value
     */
    public StochasticOscillatorDIndicator(StochasticOscillatorKIndicator k) {
        this(k, 3);
    }

    /**
     * Returns SMA value of given K indicator and timeFrame
     * @param d         Stochastic Oscillator D value
     * @param timeFrame Time frame on which average will be calculated
     */
    public StochasticOscillatorDIndicator(StochasticOscillatorDIndicator d, int timeFrame) {
        super(d);
        sma = new SMAIndicator(d, timeFrame);
    }

    /**
     * Returns SMA value of given K indicator and timeFrame
     * @param d         Stochastic Oscillator D value
     */
    public StochasticOscillatorDIndicator(StochasticOscillatorDIndicator d) {
        this(d, 3);
    }

    @Override
    protected Decimal calculate(int index) {
        return sma.getValue(index);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + sma;
    }
}
