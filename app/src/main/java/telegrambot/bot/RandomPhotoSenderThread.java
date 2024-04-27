package telegrambot.bot;

import java.time.Instant;

public class RandomPhotoSenderThread extends Thread {
	private TelegramBot bot;
	private long lastSendedMessageTimestamp = Instant.now().getEpochSecond();
	private final long timeDelta = 10;
	private long lastChatId = -1;
	private final long updateTime = 1000;

	public RandomPhotoSenderThread(TelegramBot bot) {
		this.bot = bot;
	}

	public void run() {

		while (true) {
			long currentTime = Instant.now().getEpochSecond();

			if (currentTime - lastSendedMessageTimestamp > timeDelta && lastChatId != -1) {

				bot.sendRandomPhoto(lastChatId);

				lastSendedMessageTimestamp = currentTime;
			}

			try {
				sleep(updateTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public long getLastChatId() {
		return lastChatId;
	}

	public void setLastChatId(long lastChatId) {
		this.lastChatId = lastChatId;
	}
}
