package com.javeriana.pica.service.promo.security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class Validate_Token {

    public boolean send_token(String token) throws Exception {
        String url = "http://52.170.232.119/validate/";
        return requestHttpGet(url, token);
    }

    public static Boolean requestHttpGet(String url_to_validate, String token) throws Exception {
        URL url = new URL(url_to_validate);

        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestProperty("Content-Type", "application/json");
        connect.setRequestProperty("Accept", "application/json");
        connect.setRequestProperty("authorization", token);
        connect.setRequestMethod("GET");

        try{
            if (connect.getResponseCode() == 401){
                return false;
            }else if (connect.getResponseCode() == 200) {

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connect.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    JSONObject myResponse = new JSONObject(response.toString());

                    if (myResponse.getBoolean("is_staff")){
                        return true;
                    }
                }
            }
        }catch (Exception e) {
            // Manejar excepción
            e.printStackTrace();
        }

        return false;
    }
}
