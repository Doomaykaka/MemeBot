package telegrambot.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Config {
    public final static String PROPERTY_NAME_CHAT_ID = "chat-id";
    public final static String PROPERTY_NAME_BOT_USERNAME = "bot-username";
    public final static String PROPERTY_NAME_TOKEN = "token";
    public final static String PROPERTY_NAME_BOT_BACKEND_URL = "bot-backend-url";

    private long chatId;
    private String botUsername;
    private String botToken;
    private String botBackendUrl;

    private final String CONFIG_FILENAME = "settings.conf";

    private String path;

    public Config() {
        try {
            String separator = "";
            path = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toString();

            int dirSlashIdx = 0;
            dirSlashIdx = path.lastIndexOf("/");
            if (dirSlashIdx != -1) {
                path = path.substring(0, dirSlashIdx);
                separator = "/";
            } else {
                separator = "/";
                dirSlashIdx = path.lastIndexOf("\\");
                if (dirSlashIdx != -1) {
                    path = path.substring(0, dirSlashIdx);
                } else {
                    throw new URISyntaxException("checkRootPathString", "Bad path");
                }
            }

            dirSlashIdx = path.indexOf(separator);
            path = path.substring(dirSlashIdx + 1);
            path = path + separator + CONFIG_FILENAME;

        } catch (URISyntaxException e) {
            System.out.println("Root path error");
        }
    }

    public Config(String path) {
        this.path = path;
    }

    public void readConfig() {
        Properties prop = new Properties();

        if (!path.equals("")) {
            try {
                Path filePath = Paths.get(path);
                List<String> lines = Files.readAllLines(filePath);
                for (String line : lines) {
                    prop.load(new StringReader(line));
                }
            } catch (FileNotFoundException e) {
                System.out.println("Config file not parsed");
            } catch (IOException e) {
                System.out.println("Config file not parsed");
            }
        }

        if (prop.getProperty(PROPERTY_NAME_CHAT_ID) != null) {
            chatId = Long.parseLong(prop.getProperty(PROPERTY_NAME_CHAT_ID));
        }

        if (prop.getProperty(PROPERTY_NAME_BOT_USERNAME) != null) {
            botUsername = prop.getProperty(PROPERTY_NAME_BOT_USERNAME);
        }

        if (prop.getProperty(PROPERTY_NAME_TOKEN) != null) {
            botToken = prop.getProperty(PROPERTY_NAME_TOKEN);
        }

        if (prop.getProperty(PROPERTY_NAME_BOT_BACKEND_URL) != null) {
            botBackendUrl = prop.getProperty(PROPERTY_NAME_BOT_BACKEND_URL);
        }
    }

    public long getChatId() {
        return chatId;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotBackendUrl() {
        return botBackendUrl;
    }
}
