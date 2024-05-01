package telegrambot.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import telegrambot.App;
import telegrambot.bot.BotRequests;
import telegrambot.bot.TaskThread;

/**
 * Recipient of telegram bot configuration
 * 
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class Config {
	/**
	 * Name of the chat ID parameter
	 */
	public final static String PROPERTY_NAME_CHAT_ID = "chat-id";
	/**
	 * Name of the bot username parameter
	 */
	public final static String PROPERTY_NAME_BOT_USERNAME = "bot-username";
	/**
	 * Name of the token parameter
	 */
	public final static String PROPERTY_NAME_TOKEN = "token";
	/**
	 * Name of the server url parameter
	 */
	public final static String PROPERTY_NAME_BOT_BACKEND_URL = "bot-backend-url";
	/**
	 * Name of the server login parameter
	 */
	public final static String PROPERTY_NAME_BACKEND_LOGIN = "backend-login";
	/**
	 * Name of the server password parameter
	 */
	public final static String PROPERTY_NAME_BACKEND_PASSWORD = "backend-password";

	/**
	 * Chat ID with which the telegram bot will work
	 */
	public final long chatId;
	/**
	 * Bot username
	 */
	public final String botUsername;
	/**
	 * Token for authorization on the server
	 */
	public final String botToken;
	/**
	 * Server url
	 */
	public final String botBackendUrl;
	/**
	 * Server login
	 */
	public final String backendLogin;
	/**
	 * Server password
	 */
	public final String backendPassword;

	/**
	 * File name with bot settings
	 */
	private final String CONFIG_FILENAME = "settings.conf";

	/**
	 * Path to the bot settings file
	 */
	private Path path;

	/**
	 * A constructor that reads telegram bot settings from a file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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

	/**
	 * Method that reads a parameter from an object that stores settings
	 * 
	 * @param prop
	 *            settings storage
	 * @param propertyName
	 *            name of the setting whose value needs to be retrieved from the
	 *            storage
	 * @return setting string value
	 */
	private String getProperty(Properties prop, String propertyName) throws IOException {
		if (prop.getProperty(propertyName) == null) {
			throw new IOException("Param " + propertyName + " not parsed");
		}

		return prop.getProperty(propertyName);
	}
}
