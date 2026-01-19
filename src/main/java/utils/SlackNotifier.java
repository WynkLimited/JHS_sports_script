package utils;

import com.qa.xstream.utils.CommonUtils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class SlackNotifier {
    public void sendSlackMessage(String messageText) {
        Properties prop = CommonUtils.init_prop();
        try {
            String webhookUrl = System.getenv("WEBHOOK_URL");

            if (webhookUrl == null || webhookUrl.isEmpty()) {
                throw new RuntimeException("TEAMS_WEBHOOK_URL not set!");
            }else {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
