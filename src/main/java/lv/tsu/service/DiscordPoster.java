package lv.tsu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lv.tsu.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class DiscordPoster {

    private final H2Service h2Service;
    private final String webhookUrl = System.getenv("QUARRY_WEBHOOK");
    // 6'000 char limit per post. Check for limit.
    // 25 fields max.

    @Autowired
    public DiscordPoster(H2Service h2Service) {
        this.h2Service = h2Service;
    }

    public String buildEmbedPayload(List<Item> items) {
        try {
            // Create the ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Root JSON object
            ObjectNode root = objectMapper.createObjectNode();
            root.put("username", "Quarry");
            root.put("avatar_url", "https://cdn.discordapp.com/avatars/1318213558080770120/336aa053583ac22202bd2dbf32fec490.jpg?size=128");

            // Embed JSON array
            ArrayNode embeds = root.putArray("embeds");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm");
            String formattedDate = now.format(formatter);

            // Single embed object
            ObjectNode embed = embeds.addObject();
            embed.put("title", "Item Intake Report");
            embed.put("description", "name x quantity [damageNBT]");
            embed.put("url", "");
            embed.put("color", 65280);

            // Fields array inside the embed
            ArrayNode fields = embed.putArray("fields");

            // Footer object
            ObjectNode footer = embed.putObject("footer");
            footer.put("text", formattedDate);
            footer.put("icon_url", "https://cdn.discordapp.com/avatars/1318213558080770120/336aa053583ac22202bd2dbf32fec490.jpg?size=32");

            // Image object
            ObjectNode image = embed.putObject("image");
            image.put("url", "https://static1.millenium.org/article_old/images/contenu/actus/minecraft/listequarry.gif");


            // TODO:
            // Get full list of items. Sort DESC.
            // Truncate to 25 items. Send.
            // Botch implementation for now:

            // TODO:
            // Turn it into a persistent database.
            // Display the data on a Grafana dashboard!

            int count = 1;

            // Add fields
            for (Item item : items) {
                if (count > 25) {
                    break;
                }
                ObjectNode tmpField = fields.addObject();
                tmpField.put("name", "");
                // minecraft:cobblestone x 144 [0]
                String nameNoMod = item.getItemName().split(":")[1];
                String fString = nameNoMod+" x "+item.getItemAmount();
                if (item.getItemDamage() != 0) {
                    fString += " ["+item.getItemDamage()+"]";
                }
                tmpField.put("value", fString);
                tmpField.put("inline", count % 2 == 0);
                count++;
            }

            // Convert the entire JSON object to a String
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean sendEmbed(String payload) {
        try {
            // Create URL object for the webhook
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method and headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true); // To send data in the request body

            // Write the payload to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get response code to confirm the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("Message sent successfully!");
            } else {
                System.out.println(connection.getResponseMessage());
                System.out.println("Failed to send message. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendMessage(String msg) {
        try {
            // Prepare the simple JSON payload (plain text message)
            String payload = "{\n" +
                    "  \"content\": \"" + msg + "\"\n" +
                    "}";

            // Create URL object for the webhook
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method and headers
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true); // To send data in the request body

            // Write the payload to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get response code to confirm the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                System.out.println("Message sent successfully!");
            } else {
                System.out.println("Failed to send message. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
