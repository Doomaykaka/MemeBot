package telegrambot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.bot.BotInitializer;
import telegrambot.config.Config;

public class App {
	private static Config botConfig;
	private static Logger log;

	public static void main(String[] args) throws FileNotFoundException, IOException, TelegramApiException {
		log = Logger.getLogger("appLog");

		botConfig = new Config();

		log.info("Config read");

		BotInitializer.initialize();
	}

	public static Config getBotConfig() {
		return botConfig;
	}

	public static Logger getLog() {
		return log;
	}
}
