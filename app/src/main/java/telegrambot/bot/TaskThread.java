package telegrambot.bot;

import java.time.ZonedDateTime;
import telegrambot.App;

/**
 * A thread that obtains the time after which the task will be executed, waits
 * for this time, and executes the task
 *
 * @see TelegramBot
 * @see App
 * @see TelegramBot
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class TaskThread extends Thread {
    /**
     * Telegram bot
     */
    private TelegramBot bot;
    /**
     * ID of the chat for which the task needs to be performed
     */
    private long lastChatId = -1;

    /**
     * A constructor that sets an external dependency on the telegram bot object
     *
     * @param bot
     *            telegram bot that will carry out the task
     */
    public TaskThread(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * A method that is the body of a thread, receives the time after which the task
     * will be completed, waits for this time and executes the task
     */
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

            bot.executeRemoteCommand(lastChatId, "get_random_task");
        }
    }

    /**
     * Method for getting the ID of the chat for which the task needs to be
     * completed
     *
     * @return ID of the chat for which the task needs to be performed
     */
    public long getLastChatId() {
        return lastChatId;
    }

    /**
     * Method for changing the ID of the chat for which the task needs to be
     * completed
     *
     * @param lastChatId
     *            ID of the chat for which the task needs to be performed
     */
    public void setLastChatId(long lastChatId) {
        this.lastChatId = lastChatId;
    }
}
