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
package eu.verdelhan.ta4j.mocks;

import eu.verdelhan.ta4j.Tick;
import eu.verdelhan.ta4j.TimeSeries;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;

/**
 * A time series with sample data.
 */
public class MockTimeSeries extends TimeSeries {

    /**
     * <p>Generate timeSeries with given closePrices.</p>
     * @param data closePrices for timeSeries.
     */
    public MockTimeSeries(double... data) {
        super(doublesToTicks(data));
    }

    /**
     * <p>Generate timeSeries with given Ticks.</p>
     * @param ticks Ticks for timeSeries.
     */
    public MockTimeSeries(List<Tick> ticks) {
        super(ticks);
    }

    public MockTimeSeries(double[] data, DateTime[] times) {
        super(doublesAndTimesToTicks(data, times));
    }

    public MockTimeSeries(DateTime... dates) {
        super(timesToTicks(dates));
    }

    /**
     * <p>Generate random ticks with a specific seed</p>
     * @param numOfTicks number of generated ticks.
     * @param seed Number used for Random Generator.
     */
    public MockTimeSeries(int numOfTicks, long seed)
    {
        super(arbitraryTicks(numOfTicks, seed));
    }

    /**
     * <p>Generate random TimeSeries</p>
     * @param numOfTicks number of generated ticks.
     */
    public MockTimeSeries(int numOfTicks) {
        super(arbitraryTicks(numOfTicks));
    }

    //========================================================================

    private static List<Tick> doublesToTicks(double... data) {
        ArrayList<Tick> ticks = new ArrayList<Tick>();
        for (int i = 0; i < data.length; i++) {
            ticks.add(new MockTick(new DateTime().withMillisOfSecond(i), data[i]));
        }
        return ticks;
    }

    private static List<Tick> doublesAndTimesToTicks(double[] data, DateTime[] times) {
        if (data.length != times.length) {
            throw new IllegalArgumentException();
        }
        ArrayList<Tick> ticks = new ArrayList<Tick>();
        for (int i = 0; i < data.length; i++) {
            ticks.add(new MockTick(times[i], data[i]));
        }
        return ticks;
    }

    private static List<Tick> timesToTicks(DateTime... dates) {
        ArrayList<Tick> ticks = new ArrayList<Tick>();
        int i = 1;
        for (DateTime date : dates) {
            ticks.add(new MockTick(date, i++));
        }
        return ticks;
    }

    /**
     *<p>Generate random ticks with a specific seed</p>
     *
     * @param numOfTicks Number of generated ticks.
     * @param seed Number used for Random Generator.
     * @return List of Ticks.
     */
    private static List<Tick> arbitraryTicks(int numOfTicks, long seed)
    {
        System.out.println("Random seed is: " + seed);  // Print seed to prevent confusion with other func.
        ArrayList<Tick> ticks   = new ArrayList<Tick>();
        Random rand             = new Random(seed);
        // Random value between 50 and 300
        double randomValClose   = 50 + 299*rand.nextDouble();
        double randomVolume     = 10000 + 40000*rand.nextDouble();
        for (double i = 0d; i < numOfTicks; i++)
        {
            // Random wave movement of -4 until + 4
            randomValClose          = randomValClose + ((8)*rand.nextDouble() - 4);
            double randomValMax     = randomValClose + 3*rand.nextDouble();
            double randomValMin     = randomValClose - 3*rand.nextDouble();
            double randomValOpen    = randomValMin + (randomValMax-randomValMin)*rand.nextDouble();
            randomVolume            = randomVolume + ((16000)*rand.nextDouble() - 8000);
            ticks.add(new MockTick(new DateTime(0), randomValOpen, randomValClose, randomValMax, randomValMin, i + 4, randomVolume, (int) (i + 6)));
        }
        return ticks;
    }

    /**
     *<p>Generate random ticks</p>
     *
     * @param numOfTicks Number of generated ticks.
     * @return List of Ticks.
     */
    private static List<Tick> arbitraryTicks(int numOfTicks)
    {
        long seed = System.currentTimeMillis();
        return arbitraryTicks(numOfTicks, seed);
    }
}
