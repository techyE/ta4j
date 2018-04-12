package trading.rules

import eu.verdelhan.ta4j.trading.rules.AbstractRule
import eu.verdelhan.ta4j.{Rule, TradingRecord}

/**
  * Checks satisfaction between two given rules using OR operator.
  */
class OrRule(var rule1: Rule, var rule2: Rule) extends AbstractRule {

    def isSatisfied(index: Int, tradingRecord: TradingRecord): Boolean = {
        val satisfied: Boolean = rule1.isSatisfied(index, tradingRecord) || rule2.isSatisfied(index, tradingRecord)
        traceIsSatisfied(index, satisfied)
        satisfied
    }
}

