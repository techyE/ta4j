package trading.rules

import eu.verdelhan.ta4j.trading.rules.AbstractRule
import eu.verdelhan.ta4j.{Rule, TradingRecord}

/**
  * Checks satisfaction between two given rules using AND operator.
  */
class AndRule(rule1: Rule, rule2: Rule) extends AbstractRule {

    def isSatisfied(index: Int, tradingRecord: TradingRecord): Boolean = {
        val satisfied: Boolean = rule1.isSatisfied(index, tradingRecord) && rule2.isSatisfied(index, tradingRecord)
        traceIsSatisfied(index, satisfied)
        satisfied
    }
}
