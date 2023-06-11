package com.project.linguaitalianaua;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataBuilder {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/linguauadb";
        String username = "goky";
        String password = "2001a2001a";
        String filePathWords = "src/main/java/com/project/linguaitalianaua/words.json";
        String filePathSentences = "src/main/java/com/project/linguaitalianaua/sentences.json";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObjectWord = (JSONObject) parser.parse(new FileReader(filePathWords));
            JSONObject jsonObjectSentence = (JSONObject) parser.parse(new FileReader(filePathSentences));

            String insertQuery2 = "INSERT INTO words (ukrainian, italian) VALUES (?, ?)";
            String insertQuery3 = "INSERT INTO sentence (ukrainian, italian) VALUES (?, ?)";
            PreparedStatement words = connection.prepareStatement(insertQuery2);
            PreparedStatement sentences = connection.prepareStatement(insertQuery3);

            // Iterate over the JSON object and insert the data into the table
            for (Object obj : jsonObjectWord.keySet()) {
                String ukrainian = (String) obj;
                String italian = (String) jsonObjectWord.get(ukrainian);

                words.setString(1, ukrainian);
                words.setString(2, italian);
                words.addBatch();
            }

            for (Object obj : jsonObjectSentence.keySet()) {
                String ukrainian = (String) obj;
                String italian = (String) jsonObjectSentence.get(ukrainian);

                sentences.setString(1, ukrainian);
                sentences.setString(2, italian);
                sentences.addBatch();
            }

            int[] wordsarr = words.executeBatch();
            int[] sentencesarr = sentences.executeBatch();
            System.out.println("Data inserted successfully!");

        } catch (SQLException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
