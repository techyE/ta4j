package trading.rules

import eu.verdelhan.ta4j.{Decimal, Indicator, TradingRecord}
import eu.verdelhan.ta4j.indicators.helpers.CrossIndicator
import eu.verdelhan.ta4j.indicators.simple.ConstantIndicator
import eu.verdelhan.ta4j.trading.rules.AbstractRule

/**
  * Indicates when firstIndicator crosses secondIndicator down
  * @param firstIndicator The main indicator
  * @param secondIndicator The indicator that need to be crossed
  */
class CrossedDownIndicatorRule(firstIndicator: Indicator[Decimal], secondIndicator: Indicator[Decimal]) extends AbstractRule {

    private val cross: CrossIndicator = new CrossIndicator(firstIndicator, secondIndicator)

    /**
      * Indicates crossing down a constant value
      * @param indicator Indicator on which satisfaction is being calculated
      * @param decimalVal Value that need to be crossed
      */
    def this(indicator: Indicator[Decimal], decimalVal: Decimal){
        this(indicator, new ConstantIndicator[Decimal](decimalVal))
    }

    def isSatisfied(index: Int, tradingRecord: TradingRecord): Boolean = {
        val satisfied: Boolean = cross.getValue(index)
        traceIsSatisfied(index, satisfied)
        satisfied
    }
}