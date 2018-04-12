package indicators.trackers

import eu.verdelhan.ta4j.{Decimal, Indicator, TimeSeries}
import eu.verdelhan.ta4j.indicators.RecursiveCachedIndicator
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator

/**
  * Exponential Moving Average, Gives more impact to recent events rather than [[SMAIndicator]]
  * @param indicator Data on which data will be calculated
  * @param timeFrame amount of ticks that will participate in indicator calculation
  */
class EMAIndicator(indicator: Indicator[Decimal], timeFrame: Int) extends RecursiveCachedIndicator[Decimal](indicator) {

    private val multiplier: Decimal = Decimal.TWO.dividedBy(Decimal.valueOf(timeFrame + 1))

    /**
      * Calculating EMA from TimeSeries using common settings.
      * @param timeSeries Data on which data will be calculated
      * @param timeFrame amount of ticks that will participate in indicator calculation
      */
    def this(timeSeries: TimeSeries, timeFrame: Int){
        this(new ClosePriceIndicator(timeSeries), timeFrame)
    }

    override protected def calculate(index: Int): Decimal = {
        // EMA prepares calculation with SMA
        if (index + 1 < timeFrame) {
            new SMAIndicator(indicator, timeFrame).getValue(index)
        }
        else {
            val emaPrev: Decimal = getValue(index - 1)
            indicator.getValue(index).minus(emaPrev).multipliedBy(multiplier).plus(emaPrev)
        }
    }

    override def toString: String = {
        getClass.getSimpleName + " timeFrame: " + timeFrame
    }

    def getTimeFrame: Int = {
        timeFrame
    }
}
