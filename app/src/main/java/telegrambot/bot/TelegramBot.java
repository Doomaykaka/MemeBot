package telegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.App;

public class TelegramBot extends TelegramLongPollingBot {

	private RandomPhotoSenderThread senderThread;
	private final long CHAT_ID;
	private final String BOT_USERNAME;
	private final String BOT_TOKEN;

	public TelegramBot() {
		App.getLog().info("Telegram bot photo sender started");

		CHAT_ID = App.getBotConfig().getChatId();
		BOT_USERNAME = App.getBotConfig().getBotUsername();
		BOT_TOKEN = App.getBotConfig().getBotToken();

		if (senderThread == null) {
			senderThread = new RandomPhotoSenderThread(this);
			senderThread.start();
			senderThread.setLastChatId(CHAT_ID);
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		App.getLog().info("Telegram bot triggered");

		if (update.hasMessage() && update.hasMessage() && update.getMessage().getChatId() == CHAT_ID) {
			if (update.getMessage().hasText()) {
				String[] messageTextParts = update.getMessage().getText().split("\\s");

				switch (messageTextParts[0]) {
					case "/start" :
						sendMessage(CHAT_ID, "Hello");
						break;
					case "/get-media" :
						sendRandomPhoto(CHAT_ID);
						break;
					default :
						sendMessage(CHAT_ID, "Bad command");
				}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}

	private void sendMessage(Long chatId, String textToSend) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(String.valueOf(chatId));
		sendMessage.setText(textToSend);
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {

		}
	}

	private void sendPhoto(Long chatId, SendPhoto photoToSend) {
		photoToSend.setChatId(String.valueOf(chatId));
		try {
			execute(photoToSend);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public void sendRandomPhoto(long chatId) {
		SendPhoto randomPhoto = BotRequests.getRandomPhoto();
		sendPhoto(chatId, randomPhoto);
	}
}