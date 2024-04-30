package telegrambot.bot;

import java.time.ZonedDateTime;

public class TaskThread extends Thread {
	private TelegramBot bot;
	private long lastChatId = -1;

	public TaskThread(TelegramBot bot) {
		this.bot = bot;
	}

	public void run() {

		while (true) {
			ZonedDateTime nextSendTime = BotRequests.sendNextSendTaskTimeRequest();
			long currentTime = ZonedDateTime.now().toEpochSecond();
			long nextTime = nextSendTime.toEpochSecond();

			long updateDelta = nextTime - currentTime;

			try {
				sleep(updateDelta * 1000);
			} catch (InterruptedException e) {
				break;
			}

			bot.sendRandomMedia(lastChatId);;
		}
	}

	public long getLastChatId() {
		return lastChatId;
	}

	public void setLastChatId(long lastChatId) {
		this.lastChatId = lastChatId;
	}
}
