package com.example.riskfactors.services;

import com.example.riskfactors.model.RiskFactors;
import com.example.riskfactors.repository.RiskFactorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Service
@Slf4j
public class DBService {

    @Autowired
    private RiskFactorsRepository riskFactorsRepository;


    public boolean saveInstance(RiskFactors riskFactors) { //change to bool return

        try {
            String target = riskFactors.getTarget();
            List<RiskFactors> riskFactorsToUpdate = riskFactorsRepository.findByTarget(target);
            if (riskFactorsToUpdate.isEmpty() == false) {

                RiskFactors riskFactorsInstanceToUpdate = riskFactorsRepository.findByTarget(target).get(0);
                Long id = riskFactorsInstanceToUpdate.getRiskFactorsId();

                riskFactorsInstanceToUpdate = riskFactors;
                riskFactorsInstanceToUpdate.setRiskFactorsId(id);
                log.info("Updated in DB.");
                return true;

            }
            log.info("Saved do DB.");
            riskFactorsRepository.save(riskFactors);
            return true;


        } catch (Exception e) {
            log.error("Failed to save to db");
            e.printStackTrace();
            return false;
        }

    }

    public RiskFactors getInstance(String target) {

        if (riskFactorsRepository.findByTarget(target).isEmpty() == false) {
            return riskFactorsRepository.findByTarget(target).get(0);
        }


        return null;
    }


}
