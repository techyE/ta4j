package indicators.trackers

import eu.verdelhan.ta4j.indicators.CachedIndicator
import eu.verdelhan.ta4j.{Decimal, Indicator, TimeSeries}
import eu.verdelhan.ta4j.indicators.helpers.{AverageGainIndicator, AverageLossIndicator}
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator

/**
  * Name: Relative strength index indicator.                             <br>
  * Show in % the number of up ticks out of a time frame.                <br><b>
  * Buy on Oversold, Sell on OverBought                                  </b><br>
  * RSI tends to fluctuate between 40 and 90 in a bull market            <br>
  * RSI tends to fluctuate between 10 and 60 in a bear market            <br>
  * Pros:<br>
  * Cons:<br>
  *     1. Not sensitive enough, can miss good trends easily.<br>
  *        StochRSI Indicator comes to compensate for that.<br>
  * @param indicator Data on which indicator will be calculated
  * @param timeFrame amount of ticks which will participate in indicator calculation
  */
class RSIIndicator(indicator: Indicator[Decimal], timeFrame: Int) extends CachedIndicator[Decimal](indicator){

    val averageGainIndicator = new AverageGainIndicator(indicator, timeFrame)
    val averageLossIndicator = new AverageLossIndicator(indicator, timeFrame)

    /**
      * RSI Indicator is usually calculated on Close Price
      * @param series Data on which indicator will be calculated
      * @param timeFrame amount of ticks which will participate in indicator calculation
      */
    def this(series: TimeSeries, timeFrame: Int) {
        this(new ClosePriceIndicator(series), timeFrame)
    }

    protected def calculate(index: Int): Decimal = {
        Decimal.HUNDRED.minus(Decimal.HUNDRED.dividedBy(Decimal.ONE.plus(relativeStrength(index))))
    }

    override def toString: String = {
        getClass.getName + " timeFrame: " + timeFrame
    }

    private def relativeStrength(index: Int): Decimal = {
        if (index == 0) {
            Decimal.ZERO
        } else {
            val averageGain: Decimal = averageGainIndicator.getValue(index)
            val averageLoss: Decimal = averageLossIndicator.getValue(index)
            return averageGain.dividedBy(averageLoss)
        }
    }

    def getTimeFrame: Int = {
        return timeFrame
    }

}
