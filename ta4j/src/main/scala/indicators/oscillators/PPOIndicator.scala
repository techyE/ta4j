package indicators.oscillators

import eu.verdelhan.ta4j.{Decimal, Indicator}
import eu.verdelhan.ta4j.indicators.CachedIndicator
import indicators.trackers.EMAIndicator

/**
  * Price Percentage Oscillator Indicator.<br>
  * Similar to [[indicators.trackers.MACDIndicator]], determined by Percentage instead of absolute value
  * @param shortTermEma EMA with short time frame
  * @param longTermEma EMA with long time frame
  */
class PPOIndicator(shortTermEma: EMAIndicator, longTermEma: EMAIndicator) extends CachedIndicator[Decimal](shortTermEma) {

    // In order for the indicator to work ShortTermEMA must bi smaller than LongTermEMA
    if (shortTermEma.getTimeFrame > longTermEma.getTimeFrame) {
        throw new IllegalArgumentException("Long term period count must be greater than short term period count")
    }

    /**
      * @param indicator The indicator on which EMA indicator will be performed
      * @param shortTimeFrame The time frame for calculating ShortTermEMA
      * @param longTimeFrame The time frame for calculating LongTermEMA
      */
    def this (indicator: Indicator[Decimal], shortTimeFrame: Int, longTimeFrame: Int) {
        this(new EMAIndicator(indicator, shortTimeFrame), new EMAIndicator(indicator, longTimeFrame))
    }

    protected def calculate (index: Int): Decimal = {
        val shortEmaValue: Decimal = shortTermEma.getValue (index)
        val longEmaValue: Decimal = longTermEma.getValue (index)
        shortEmaValue.minus(longEmaValue).dividedBy(longEmaValue).multipliedBy(Decimal.HUNDRED)
    }
}