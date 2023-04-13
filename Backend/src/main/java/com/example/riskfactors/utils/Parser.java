package com.example.riskfactors.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Parser {

    public JSONObject getJson(String fileName) {
        log.info("Creating Json Object From File: " + fileName);
        File file = new File(fileName);
        try {
            String content = FileUtils.readFileToString(file, String.valueOf(StandardCharsets.UTF_8));
            JSONObject obj = new JSONObject(content);
            if (!obj.isEmpty()) {
                return obj;
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error("Exception : " + e.getMessage());
            return null;
        }
    }

    public String convert(String fileName) throws IOException, JSONException {
        log.info("Creating JSON String From File: " + fileName);
        String line = "";
        String str = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null)
                str += line;
            JSONObject json = XML.toJSONObject(str);
            this.deleteFile(fileName);
            return json.toString();
        } catch (IOException | JSONException e) {
            log.error("Exception : " + e.getMessage());
            return null;
        }
    }

    public boolean deleteFile(String fileName) {
        log.info("Deleting File: " + fileName);
        File file = new File(fileName);
        if (file.delete()) {
            log.info("Deleted file " + fileName);
            return true;
        }
        log.error("Failed to delete file: " + fileName);
        return false;
    }

    public static String standardizeUrl(String url) {

        if (!url.startsWith("www.")) {
            url = "www." + url;
        }
        url = url.replaceAll("^(http://|https://)", "");
        url = url.replaceAll("/+$", "");
        url = url.toLowerCase();

        try {
            URI uri = new URI(url);
            return uri.normalize().toString();
        } catch (URISyntaxException e) {
            log.error("Error Parsing The Url.");
            e.printStackTrace();
            return null;
        }
    }

}
