package com.javeriana.pica.service.promo.repository;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QueryProduct {

    public String query_elastic(String idProduct) throws Exception {
        String url = "http://201.226.152.89:9200/producto/deportes/_search?q="+idProduct;
        return requestHttpGet(url);
    }

    public static String requestHttpGet(String url_to_validate) throws Exception {
        URL url = new URL(url_to_validate);

        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestProperty("Content-Type", "application/json");
        connect.setRequestProperty("Accept", "application/json");
        connect.setRequestMethod("GET");

        try{
            if (connect.getResponseCode() == 401){
                return " ";
            }else if (connect.getResponseCode() == 200) {

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connect.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JSONObject myResponse = new JSONObject(response.toString());

                    return myResponse.getString("hits");
                }
            }
        }catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }

        return " ";
    }
}
