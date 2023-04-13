package com.example.riskfactors.utils;

import com.example.riskfactors.model.RiskFactors;
import org.springframework.stereotype.Component;

@Component
public class RiskFactorsRiskNumberCalculation {
    public RiskFactors calculateRiskNumber(RiskFactors riskFactors) {
        if (riskFactors.getApplicationSecurityFactors() != null) {
            riskFactors.setRiskNumber(riskFactors.getRiskNumber() + riskFactors.getApplicationSecurityFactors().getRiskNumber());
        }
        if (riskFactors.getNetworkSecurityFactors() != null) {
            riskFactors.setRiskNumber(riskFactors.getRiskNumber() + riskFactors.getNetworkSecurityFactors().getRiskFactor());
        }
        if (riskFactors.getIpReputation() != null) {
            riskFactors.setRiskNumber(riskFactors.getRiskNumber() + riskFactors.getIpReputation().getRiskFactor());
        }
        if (riskFactors.getDnsHealth() != null) {
            riskFactors.setRiskNumber(riskFactors.getRiskNumber() + riskFactors.getDnsHealth().getRiskScore());
        }


        return riskFactors;
    }
}
