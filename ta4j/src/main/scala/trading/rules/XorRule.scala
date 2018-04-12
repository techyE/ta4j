package trading.rules

import eu.verdelhan.ta4j.trading.rules.AbstractRule
import eu.verdelhan.ta4j.{Rule, TradingRecord}


/**
  * Checks satisfaction between two given rules using XOR operator.
  */
class XorRule(var rule1: Rule, var rule2: Rule) extends AbstractRule {

    override def isSatisfied(index: Int, tradingRecord: TradingRecord): Boolean = {
        val satisfied = rule1.isSatisfied(index, tradingRecord) ^ rule2.isSatisfied(index, tradingRecord)
        traceIsSatisfied(index, satisfied)
        satisfied
    }
}
