package core.detection;

import core.features.FlowFeatures;

public class RuleEngine {

    public RuleDecision evaluate(FlowFeatures flow) {

        int rulesTriggered = 0;

        // Rule 1
        if (flow.count > RuleConfig.COUNT_THRESHOLD) {
            rulesTriggered++;
        }

        // Rule 2
        if (flow.serrorRate > RuleConfig.SERROR_RATE_THRESHOLD) {
            rulesTriggered++;
        }

        // Rule 3
        if (flow.dstHostCount > RuleConfig.DST_HOST_COUNT_THRESHOLD) {
            rulesTriggered++;
        }

        // Rule 4
        if (flow.rerrorRate > RuleConfig.RERROR_RATE_THRESHOLD) {
            rulesTriggered++;
        }

        RuleDecision decision = new RuleDecision();

        // Decision logic
        if (rulesTriggered == 0) {
            decision.label = "NORMAL";
            decision.confidence = 0.9;
            decision.severity = "NONE";
        }
        else if (rulesTriggered == 1) {
            decision.label = "ATTACK";
            decision.confidence = 0.7;
            decision.severity = "LOW";
        }
        else if (rulesTriggered == 2) {
            decision.label = "ATTACK";
            decision.confidence = 0.8;
            decision.severity = "MEDIUM";
        }
        else {
            decision.label = "ATTACK";
            decision.confidence = 0.95;
            decision.severity = "HIGH";
        }

        return decision;
    }
}