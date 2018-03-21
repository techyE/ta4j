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

import eu.verdelhan.ta4j.Decimal;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.CachedIndicator;
import eu.verdelhan.ta4j.indicators.helpers.DirectionalDownIndicator;
import eu.verdelhan.ta4j.indicators.helpers.DirectionalUpIndicator;

/**
 * <p>
 * Name: Directional movement indicator.      <br>
 * Indicator Sign: DX
 * Type: Lagging.
 * </p>
 */
public class DirectionalMovementIndicator extends CachedIndicator<Decimal>{

    private final int timeFrame;
    private final DirectionalUpIndicator dirUp;
    private final DirectionalDownIndicator dirDown;

    public DirectionalMovementIndicator(TimeSeries series, int timeFrame) {
        super(series);
        this.timeFrame = timeFrame;
        this.dirUp     = new DirectionalUpIndicator(series, timeFrame);
        this.dirDown   = new DirectionalDownIndicator(series, timeFrame);
    }

    public DirectionalMovementIndicator(DirectionalUpIndicator dirUp, DirectionalDownIndicator dirDown) {
        super(dirUp);

        if (dirUp.getTimeFrame() != dirDown.getTimeFrame())
            try {throw new Exception("Received Indicators with different timeFrames. Cause to bad calculations");}
            catch (Exception e) {e.printStackTrace();}

        this.timeFrame = dirUp.getTimeFrame();
        this.dirUp     = dirUp;
        this.dirDown   = dirDown;
    }

    @Override
    protected Decimal calculate(int index)
    {
        if (index == 0)
            return Decimal.ZERO;

        Decimal dirUpVal    = dirUp.getValue(index);
        Decimal dirDownVal  = dirDown.getValue(index);
        return dirUpVal.minus(dirDownVal).abs().dividedBy(dirUpVal.plus(dirDownVal)).multipliedBy(Decimal.HUNDRED);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + timeFrame;
    }

    public int getTimeFrame() {
        return timeFrame;
    }
}
