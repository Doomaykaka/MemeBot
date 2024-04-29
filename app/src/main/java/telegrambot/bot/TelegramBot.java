package telegrambot.bot;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.App;
import telegrambot.models.Task;

public class TelegramBot extends TelegramLongPollingBot {

	private RandomSenderThread senderThread;
	private final long CHAT_ID;
	private final String BOT_USERNAME;
	private final String BOT_TOKEN;

	public TelegramBot() {
		App.getLog().info("Telegram bot sender started");

		CHAT_ID = App.getBotConfig().getChatId();
		BOT_USERNAME = App.getBotConfig().getBotUsername();
		BOT_TOKEN = App.getBotConfig().getBotToken();

		if (senderThread == null) {
			senderThread = new RandomSenderThread(this);
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
					case "/recheck-tasks" :
						senderThread.interrupt();
						senderThread = new RandomSenderThread(this);
						senderThread.start();
						senderThread.setLastChatId(CHAT_ID);
					case "/start" :
						sendMessage(CHAT_ID, "Hello");
						break;
					case "/get-media" :
						sendRandomMedia(CHAT_ID);
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
			e.printStackTrace();
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

	public void sendRandomMedia(long chatId) {
		Task backendTask = BotRequests.getTask();

		switch (backendTask.getType()) {
			case "TEXT" :
				sendMessage(CHAT_ID, backendTask.getContent());
				break;
			case "IMAGE" :
				InputStream imgStream = new ByteArrayInputStream(
						Base64.getDecoder().decode(backendTask.getContent().getBytes()));
				SendPhoto photoToSend = new SendPhoto();
				photoToSend.setPhoto(new InputFile(imgStream, "image.jpg"));

				sendPhoto(CHAT_ID, photoToSend);
				break;
			default :
				App.getLog().info("Media not parsed");
		}
	}
}