package com.example.riskfactors.repository;

import com.example.riskfactors.model.RiskFactors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RiskFactorsRepository extends CrudRepository<RiskFactors, Long> {
    List<RiskFactors> findByTarget(String target);
}
