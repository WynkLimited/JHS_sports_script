package utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackNotifier {
    public void sendSlackMessage(String messageText) {
        try {
            String webhookUrl = "https://hooks.slack.com/services/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";
            String payload = "{\"text\": \"" + messageText.replace("\"", "\\\"") + "\"}";

            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Slack response code: " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
