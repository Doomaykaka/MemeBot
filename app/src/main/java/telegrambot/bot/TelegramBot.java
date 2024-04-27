package telegrambot.bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import okhttp3.ResponseBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import retrofit2.Call;
import retrofit2.Response;
import telegrambot.models.TelegramBotBackendService;

public class TelegramBot extends TelegramLongPollingBot {

	private RandomPhotoSenderThread senderThread;
	private final long chatId = -938033073;

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.hasMessage() && update.getMessage().getChatId() == chatId) {

			if (senderThread == null) {
				senderThread = new RandomPhotoSenderThread(this);
				senderThread.start();
			}

			if (update.getMessage().hasText()) {
				String[] messageTextParts = update.getMessage().getText().split("\\s");
				long chatId = update.getMessage().getChatId();
				senderThread.setLastChatId(chatId);

				switch (messageTextParts[0]) {
					case "/start" :
						sendMessage(chatId, "Start");
						break;
					case "/get-media" :
						sendRandomPhoto(chatId);
						break;
					default :
						sendMessage(chatId, "Default");
				}
			}
		}
	}

	@Override
	public String getBotUsername() {
		return "MyAmazingBot";
	}

	@Override
	public String getBotToken() {
		return "7013070541:AAFDjrkuwJGaqPoC_Jxel7VpT3fmdOccwak";
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
		boolean asAttachment = true;

		TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
		Call<ResponseBody> response = botService.getMedia(asAttachment);

		InputStream dataInputStream;

		Response<ResponseBody> responseBody = null;
		try {
			responseBody = response.execute();

			if (responseBody != null) {
				String contentType = responseBody.headers().get("Content-Type");
				String mediaFilePreffix = contentType.substring(contentType.indexOf("/") + 1, contentType.length());

				dataInputStream = responseBody.body().byteStream();

				SendPhoto message = new SendPhoto();
				message.setPhoto(new InputFile(dataInputStream, "media." + mediaFilePreffix));
				sendPhoto(chatId, message);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}