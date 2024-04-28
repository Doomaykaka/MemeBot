package telegrambot.bot;

import java.time.ZonedDateTime;

public class RandomPhotoSenderThread extends Thread {
    private TelegramBot bot;
    private long lastChatId = -1;

    public RandomPhotoSenderThread(TelegramBot bot) {
        this.bot = bot;
    }

    public void run() {

        while (true) {
            ZonedDateTime nextSendTime = BotRequests.sendNextSendPhotoTimeRequest();
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
}
