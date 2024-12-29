package com.example;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FreeWhatsAppService {

    public static void sendWhatsAppMessage(String phoneNumber, String message) {
        try {
            // Replace with your Phone Number ID and Access Token
            String apiUrl = "https://graph.facebook.com/v17.0/your_phone_number_id/messages";
            String accessToken = "your_access_token";

            // Create JSON payload
            String payload = String.format(
                "{ \"messaging_product\": \"whatsapp\", \"to\": \"%s\", \"type\": \"text\", \"text\": { \"body\": \"%s\" } }",
                phoneNumber, message
            );

            // Create HTTP connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write the payload
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            // Handle response
            if (connection.getResponseCode() == 200) {
                System.out.println("WhatsApp message sent successfully!");
            } else {
                System.out.println("Error sending WhatsApp message: " + connection.getResponseMessage());
            }

        } catch (Exception e) {
            System.err.println("Failed to send WhatsApp message: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Replace with the recipient's phone number (in E.164 format)
        String recipientPhoneNumber = "1234567890"; // Example: "+919876543210"
        String messageContent = "Hello! Your information has been saved successfully.";

        sendWhatsAppMessage(recipientPhoneNumber, messageContent);
    }
}
