package com.example.riskfactors;

import com.example.riskfactors.utils.RunProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RiskFactorsApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(RiskFactorsApplication.class, args);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
