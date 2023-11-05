/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Interface for extracting data from the Sisu API.
 */
public interface iAPI {

    /**
     * Returns a JsonObject that is extracted from the Sisu API.
     * Works as a funnel point in the programs communication with the Sisu api since
     * all the requests go through this implementation. Static impleentation in this
     * interface itself saved from writing it multiple times in the Apiservices
     * class.
     * 
     * @param urlString URL for retrieving information from the Sisu API.
     * @return JsonObject of the requested kind.
     */
    public static JsonObject getJsonObjectFromApi(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                return jsonObject;
            } else if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                return jsonArray.get(0).getAsJsonObject();
            } else {
                throw new Exception("The JSON string is not a valid object.");
            }
        } else {
            throw new Exception("Failed to retrieve JSON from API. Response code: " + responseCode);
        }
    };

}
