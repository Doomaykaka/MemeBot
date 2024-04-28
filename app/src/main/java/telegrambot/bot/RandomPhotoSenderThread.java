package telegrambot.bot;

import java.io.IOException;
import java.time.ZonedDateTime;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import telegrambot.models.TelegramBotBackendService;

public class RandomPhotoSenderThread extends Thread {
	private TelegramBot bot;
	private long lastChatId = -1;

	public RandomPhotoSenderThread(TelegramBot bot) {
		this.bot = bot;
	}

	public void run() {

		while (true) {
			ZonedDateTime nextSendTime = sendNextSendPhotoTimeRequest();
			long currentTime = ZonedDateTime.now().toEpochSecond();
			long nextTime = nextSendTime.toEpochSecond();

			long updateDelta = nextTime - currentTime;

			try {
				sleep(updateDelta * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			bot.sendRandomPhoto(lastChatId);
		}
	}

	public long getLastChatId() {
		return lastChatId;
	}

	public void setLastChatId(long lastChatId) {
		this.lastChatId = lastChatId;
	}

	private ZonedDateTime sendNextSendPhotoTimeRequest() {
		ZonedDateTime nextSendTime = null;

		TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
		Call<ResponseBody> response = botService.getNextSendPhotoTime();

		String time;

		Response<ResponseBody> responseBody = null;
		try {
			responseBody = response.execute();

			if (responseBody != null) {
				time = responseBody.body().string();

				nextSendTime = ZonedDateTime.parse(time);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return nextSendTime;
	}
}
