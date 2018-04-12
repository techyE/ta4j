package indicators.trackers

import eu.verdelhan.ta4j.indicators.CachedIndicator
import eu.verdelhan.ta4j.{Decimal, Indicator}


/**
  * Name: Moving Average Convergence Divergence Indicator.       <br>
  * Used the stock prices difference by absolute stock prices.   <br>
  *<br>
  * When indicator > 0                       - bullish trend.    <br>
  * When indicator < 0                       - bearish trend.    <br>
  * When indicator cross up indicatorAvg     - up ticks.         <br>
  * When indicator cross down indicatorAvg   - down ticks.       <br>
  * @param shortTermEma [[EMAIndicator]] with short timeFrame
  * @param longTermEma [[EMAIndicator]] with lon timeFrame
  */
class MACDIndicator(shortTermEma: EMAIndicator, longTermEma: EMAIndicator) extends CachedIndicator[Decimal](shortTermEma){

    if (shortTermEma.getTimeFrame > longTermEma.getTimeFrame) {
        throw new IllegalArgumentException("Long term period count must be greater than short term period count")
    }

    def this(indicator: Indicator[Decimal], shortTimeFrame: Int, longTimeFrame: Int) {
        this(new EMAIndicator(indicator, shortTimeFrame), new EMAIndicator(indicator, longTimeFrame))
    }

    protected def calculate(index: Int): Decimal = {
        shortTermEma.getValue(index).minus(longTermEma.getValue(index))
    }

    def getLongTermEma: Int = {
        longTermEma.getTimeFrame
    }

    def getShortTermEma: Int = {
        shortTermEma.getTimeFrame
    }

}
