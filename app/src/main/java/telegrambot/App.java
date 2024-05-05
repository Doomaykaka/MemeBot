package telegrambot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.bot.BotInitializer;
import telegrambot.config.Config;

/**
 * The entry point of the application, which reads the configuration, creates a
 * log, and also creates a telegram bot
 *
 * @see Config
 * @see BotInitializer
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class App {
    /**
     * Telegram bot configuration
     */
    private static Config botConfig;

    /**
     * Application logger
     */
    private static Logger log;

    /**
     * The method is the entry point to the application, reads the configuration,
     * creates a log and telegram bot
     *
     * @param args
     *            array of arguments with which the application was launched
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws TelegramApiException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, TelegramApiException {
        log = Logger.getLogger("appLog");

        botConfig = new Config();

        log.info("Config read");

        BotInitializer.initialize();
    }

    /**
     * Method to get a bot configuration
     *
     * @return bot configuration
     */
    public static Config getBotConfig() {
        return botConfig;
    }

    /**
     * Method for obtaining a logger
     *
     * @return logger
     */
    public static Logger getLog() {
        return log;
    }

    public static void setBotConfig(Config botConfig) {
        App.botConfig = botConfig;
    }

    public static void setLog(Logger log) {
        App.log = log;
    }
}
