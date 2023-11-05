/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package fi.tuni.prog3.sisu;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Interface with methods to read from a file and write to a file.
 */
public interface iReadAndWriteToFile {
    /**
     * Reads JSON from the given file.
     * 
     * @param fileName name of the file to read from.
     * @return jsonelement if successfull, null otherwise
     * @throws Exception if the method e.g, cannot find the file.
     */
    public static JsonElement readFromFile(String fileName) throws Exception {
        try {
            var input = new BufferedReader(
                    new FileReader(fileName));
            JsonParser parser = new JsonParser();
            JsonElement el = parser.parse(input);

            return el;
        } catch (Exception e) {
            System.out.println("file read: " + e);
            return null;
        }
    };

    /**
     * Write the student progress as JSON into the given file.
     * 
     * @param fileName name of the file to write to.
     * @return true if the write was successful, otherwise false.
     * @throws Exception if the method e.g., cannot write to a file.
     */
    public static boolean writeToFile(String fileName, JsonElement elementToWrite) throws Exception {
        try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter(fileName);
            gson.toJson(elementToWrite, writer);
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    };
}
