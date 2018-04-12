package indicators.oscillators

import eu.verdelhan.ta4j.{Decimal, Indicator}
import eu.verdelhan.ta4j.indicators.CachedIndicator
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator


/**
  * Stochastic oscillator D.<br>
  * Performs [[SMAIndicator]] on a given indicator.
  * @param indicator Usually a [[StochasticOscillatorKIndicator]]
  * @param timeFrame Time frame on which SMA will be performed
  */
class StochasticOscillatorDIndicator(indicator: Indicator[Decimal], timeFrame: Int) extends CachedIndicator[Decimal](indicator) {
    private val sma = new SMAIndicator(indicator, timeFrame)

    /**
      * Stochastic Oscillator D common usage
      * @param indicator
      */
    def this(indicator: Indicator[Decimal]) {
        this(indicator, 3)
    }

    override protected def calculate(index: Int): Decimal = sma.getValue(index)

    override def toString: String = getClass.getSimpleName + " " + sma
}
