package indicators.oscillators

import eu.verdelhan.ta4j.{Decimal, Indicator, TimeSeries}
import eu.verdelhan.ta4j.indicators.CachedIndicator
import eu.verdelhan.ta4j.indicators.helpers.HighestValueIndicator
import eu.verdelhan.ta4j.indicators.helpers.LowestValueIndicator
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator
import eu.verdelhan.ta4j.indicators.simple.MaxPriceIndicator
import eu.verdelhan.ta4j.indicators.simple.MinPriceIndicator

/**
  * Stochastic oscillator K.
  * @param indicator Data on which indicator will be calculated
  * @param timeFrame amount of ticks which will participate in indicator calculation
  * @param maxPriceIndicator
  * @param minPriceIndicator
  */
class StochasticOscillatorKIndicator(indicator: Indicator[Decimal], timeFrame: Int, maxPriceIndicator: MaxPriceIndicator, minPriceIndicator: MinPriceIndicator) extends CachedIndicator[Decimal](indicator) {

    private val highestHigh = new HighestValueIndicator(maxPriceIndicator, timeFrame)
    private val lowestMin = new LowestValueIndicator(minPriceIndicator, timeFrame)

    /**
      * Calculates Stochastic K from a Time Series.
      * @param timeSeries Data on which indicator will be calculated
      * @param timeFrame amount of Ticks which will participate in calculation
      */
    def this(timeSeries: TimeSeries, timeFrame: Int) {
        this(new ClosePriceIndicator(timeSeries), timeFrame, new MaxPriceIndicator(timeSeries), new MinPriceIndicator(timeSeries))
    }

    override protected def calculate(index: Int): Decimal = {
        val highestHighPrice = highestHigh.getValue(index)
        val lowestLowPrice = lowestMin.getValue(index)
        indicator.getValue(index).minus(lowestLowPrice).dividedBy(highestHighPrice.minus(lowestLowPrice)).multipliedBy(Decimal.HUNDRED)
    }

    override def toString: String = getClass.getSimpleName + " timeFrame: " + timeFrame
}
