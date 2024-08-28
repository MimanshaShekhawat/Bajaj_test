package com.example;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;

public class DestinationHashGenerator {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN> <path-to-json-file>");
            System.out.println("java -jar DestinationHashGenerator.jar 240350000046 C:\\Users\\miman\\Desktop\\Mimansha_Shekhawat\\ProblemStatement1\\DestinationHashGenerator\\src\\main\\resources\\test.json");
            return;
        }

        String prn = args[0].toLowerCase().replaceAll("\\s+", "");
        String filePath = args[1];

        try {
            // Read and parse JSON file
            FileReader reader = new FileReader(new File(filePath));
            Scanner scanner = new Scanner(reader);
            StringBuilder jsonContent = new StringBuilder();

            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }

            scanner.close();
            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            // Traverse JSON to find the first "destination" key
            String destinationValue = findDestination(jsonObject);

            if (destinationValue != null) {
                // Generate a random string
                String randomString = generateRandomString(8);

                // Create the hash
                String toHash = prn + destinationValue + randomString;
                String hash = generateMD5Hash(toHash);

                // Output the result
                System.out.println(hash + ";" + randomString);
            } else {
                System.out.println("Key 'destination' not found in the JSON file.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findDestination(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);

            if (key.equals("destination")) {
                return value.toString();
            } else if (value instanceof JSONObject) {
                String result = findDestination((JSONObject) value);
                if (result != null) return result;
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object element = array.get(i);
                    if (element instanceof JSONObject) {
                        String result = findDestination((JSONObject) element);
                        if (result != null) return result;
                    }
                }
            }
        }
        return null;
    }

    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            randomString.append(characters.charAt(random.nextInt(characters.length())));
        }

        return randomString.toString();
    }

    private static String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
