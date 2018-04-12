package indicators.trackers

import eu.verdelhan.ta4j.{Decimal, Indicator, TimeSeries}
import eu.verdelhan.ta4j.indicators.CachedIndicator
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator

/**
  * Simple Moving Average Indicator.
  * @param indicator Data on which indicator will be calculated
  * @param timeFrame amount of ticks which will participate in indicator calculation
  */
class SMAIndicator(indicator: Indicator[Decimal], timeFrame: Int) extends CachedIndicator[Decimal](indicator) {

    /**
      * Calculating SMA from TimeSeries using common settings.
      * @param timeSeries Data on which data will be calculated
      * @param timeFrame amount of ticks that will participate in indicator calculation
      */
    def this(timeSeries: TimeSeries, timeFrame: Int){
        this(new ClosePriceIndicator(timeSeries), timeFrame)
    }

    override protected def calculate(index: Int): Decimal = {
        var sum: Decimal = Decimal.ZERO
        val startTick: Int = Math.max(0, index - timeFrame + 1)
        for (i <- startTick to index) {
                sum = sum.plus(indicator.getValue(i))
            }
        val realTimeFrame: Int = Math.min(timeFrame, index + 1)
        sum.dividedBy(Decimal.valueOf(realTimeFrame))
    }

    override def toString: String = {
        getClass.getSimpleName + " timeFrame: " + timeFrame
    }

    def getTimeFrame: Int = {
        timeFrame
    }
}
