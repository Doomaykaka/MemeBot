/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package telegrambot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegrambot.bot.BotInitializer;
import telegrambot.bot.TelegramBot;

public class App {
	public static void main(String[] args) {
		BotInitializer.initialize();
	}
}
