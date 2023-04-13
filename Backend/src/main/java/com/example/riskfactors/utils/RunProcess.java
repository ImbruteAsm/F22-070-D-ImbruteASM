package com.example.riskfactors.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class RunProcess {


    public Boolean runProcess(String command) {
        log.info("Running Bash Command: " + command);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
            int exitCode = process.waitFor();
            if (exitCode == 0 || exitCode == 3) {
                log.info("Scan complete!");
                return true;
            } else {
                log.info(exitCode + "");
                log.error("Unable to run command -->" + command);
                return false;
            }

        } catch (IOException e) {
            log.error("Error : " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            log.error("Error : " + e.getMessage());
            return false;
        }
    }
}
