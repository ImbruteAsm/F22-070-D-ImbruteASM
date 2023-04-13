package com.example.riskfactors.services;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//INTERFACE
public interface EnrichmentTools {

    public abstract List<String> scanApplication(String url) throws IOException, InterruptedException;

    public abstract void convertToJSON(String name) throws IOException, InterruptedException;

    public abstract ArrayList<String> parseJSONReport(String path, String check) throws IOException;
}
