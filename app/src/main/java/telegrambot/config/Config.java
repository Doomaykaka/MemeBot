package telegrambot.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
	public final static String PROPERTY_NAME_CHAT_ID = "chat-id";
	public final static String PROPERTY_NAME_BOT_USERNAME = "bot-username";
	public final static String PROPERTY_NAME_TOKEN = "token";
	public final static String PROPERTY_NAME_BOT_BACKEND_URL = "bot-backend-url";
	public final static String PROPERTY_NAME_BACKEND_LOGIN = "backend-login";
	public final static String PROPERTY_NAME_BACKEND_PASSWORD = "backend-password";

	public final long chatId;
	public final String botUsername;
	public final String botToken;
	public final String botBackendUrl;
	public final String backendLogin;
	public final String backendPassword;

	private final String CONFIG_FILENAME = "settings.conf";

	private Path path;

	public Config() throws FileNotFoundException, IOException {
		path = Path.of(Path.of(System.getProperty("user.dir"), CONFIG_FILENAME).toFile().getAbsolutePath());

		if (!path.toFile().exists()) {
			throw new FileNotFoundException("No config file: " + path.toString());
		}

		FileInputStream configFileInputStream = new FileInputStream(path.toString());
		Properties prop = new Properties();
		prop.load(configFileInputStream);

		chatId = Long.parseLong(getProperty(prop, PROPERTY_NAME_CHAT_ID));
		botUsername = getProperty(prop, PROPERTY_NAME_BOT_USERNAME);
		botToken = getProperty(prop, PROPERTY_NAME_TOKEN);
		botBackendUrl = getProperty(prop, PROPERTY_NAME_BOT_BACKEND_URL);
		backendLogin = getProperty(prop, PROPERTY_NAME_BACKEND_LOGIN);
		backendPassword = getProperty(prop, PROPERTY_NAME_BACKEND_PASSWORD);
	}

	private String getProperty(Properties prop, String propertyName) throws IOException {
		if (prop.getProperty(propertyName) == null) {
			throw new IOException("Param " + propertyName + " not parsed");
		}

		return prop.getProperty(propertyName);
	}
}
