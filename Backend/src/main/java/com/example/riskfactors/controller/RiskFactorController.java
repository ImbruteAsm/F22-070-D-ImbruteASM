package com.example.riskfactors.controller;


import com.example.riskfactors.model.RiskFactors;
import com.example.riskfactors.services.DBService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.example.riskfactors.services.Enrichment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@ResponseBody
@Slf4j
@RequestMapping


// TODO: log
// TODO: return type bool for crud functions
// TODO: clean up the code
// TODO: Push the code and make a PR (pull req)

public class RiskFactorController {

    @Autowired
    private DBService dbService;
    @Autowired
    private Enrichment enrichment;
    @Autowired
    private ObjectMapper mapper;

    @CrossOrigin(origins = "*")
    @GetMapping("/scan")
    public ResponseEntity<RiskFactors> scan(String target) throws IOException {
        try {
            log.info("Detected input :{}", target);
            // target = Parser.standardizeUrl(target);
            log.info("Normalized input :{}", target);

            RiskFactors riskFactors = enrichment.getFactors(target);
            if (riskFactors == null) {
                log.error("Failed to save to DB");
                return ResponseEntity.notFound().build();
            }
            dbService.saveInstance(riskFactors);
            log.info("Saved To DB");
            log.info("Formatted RiskFactors: \n{}", formatRiskFactors(riskFactors));

            return new ResponseEntity<>(riskFactors, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Exception in scanning network and endpoint security factors", ex.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    private String formatRiskFactors(RiskFactors riskFactors) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        String response = objectWriter.writeValueAsString(riskFactors);

        return response;
    }

    @GetMapping("/fetch")
    public ResponseEntity<RiskFactors> fetch(String target) {

        RiskFactors riskFactors = null;

        try {

            riskFactors = dbService.getInstance(target);
            if (riskFactors == null) {
                log.error("Instance does not exist in DB.");
                return ResponseEntity.notFound().build();
            }


        }
        catch (Exception e) {
            log.error("Failed to fetch from DB. ", e);
            return ResponseEntity.notFound().build();
        }
        log.info("Successfully fetched from DB.");
        return new ResponseEntity<>(riskFactors, HttpStatus.OK);

    }


}
