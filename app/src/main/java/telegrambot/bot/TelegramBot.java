package telegrambot.bot;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.App;
import telegrambot.models.Task;

/**
 * Logic of telegram bot operation and auxiliary tools
 *
 * @see TaskThread
 * @see App
 * @see BotRequests
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class TelegramBot extends TelegramLongPollingBot {
    /**
     * Thread performing tasks
     */
    private TaskThread senderThread;
    /**
     * Chat ID with which the telegram bot will work
     */
    private final long CHAT_ID;
    /**
     * Bot username
     */
    private final String BOT_USERNAME;
    /**
     * Bot token
     */
    private final String BOT_TOKEN;

    /**
     * A constructor that sets the telegram bot settings and starts scheduled tasks
     */
    public TelegramBot() {
        App.getLog().info("Telegram bot sender started");

        CHAT_ID = App.getBotConfig().chatId;
        BOT_USERNAME = App.getBotConfig().botUsername;
        BOT_TOKEN = App.getBotConfig().botToken;

        if (senderThread == null) {
            senderThread = new TaskThread(this);
            senderThread.start();
            senderThread.setLastChatId(CHAT_ID);
        }
    }

    /**
     * Method that is triggered when the state of the chat in which the telegram bot
     * is located is updated
     *
     * @param update
     *            updated chat status with telegram bot
     */
    @Override
    public void onUpdateReceived(Update update) {
        App.getLog().info("Telegram bot triggered");

        if (update.hasMessage() && update.getMessage().getChatId() == CHAT_ID) {
            if (update.getMessage().hasText()) {
                String message = update.getMessage().getText();

                if (message.substring(0, 1).equals("/")) {
                    commandExecuter(message.substring(1));
                }
            }
        }

        if (update.hasCallbackQuery()) {
            String command = update.getCallbackQuery().getData();

            commandExecuter(command);
        }
    }

    /**
     * A method that implements commands supported by a telegram bot
     *
     * @param command
     *            command received by the bot when updating the chat state
     */
    private void commandExecuter(String command) {
        command = command.replace(getBotUsername(), "");

        switch (command) {
            case "recheck_tasks" :
                senderThread.interrupt();
                senderThread = new TaskThread(this);
                senderThread.start();
                senderThread.setLastChatId(CHAT_ID);
                break;
            case "menu" :
                generateMenuButtons(CHAT_ID);
                break;
            default :
                executeRemoteCommand(CHAT_ID, command);
        }
    }

    /**
     * Method for getting bot username
     *
     * @return bot username
     */
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    /**
     * Method for getting bot token
     *
     * @return bot token
     */
    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    /**
     * Method for sending a text message to chat
     *
     * @param chatId
     *            ID of the chat to which the message should be sent
     * @param textToSend
     *            message to be sent
     */
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

    /**
     * Method for sending an image to chat
     *
     * @param chatId
     *            ID of the chat to which the message should be sent
     * @param photoToSend
     *            image to be sent
     */
    private void sendPhoto(Long chatId, SendPhoto photoToSend) {
        photoToSend.setChatId(String.valueOf(chatId));
        try {
            execute(photoToSend);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to send random media to chat
     *
     * @param chatId
     *            chat ID to send random media to
     */
    public void executeRemoteCommand(long chatId, String command) {
        Task backendTask = BotRequests.getTaskByCommand(command);

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

    /**
     * A method that generates a graphical menu for controlling a telegram bot in a
     * chat
     *
     * @param chatId
     *            chat ID for generating graphical menu
     */
    private void generateMenuButtons(long chatId) {
        // TODO: Create dynamically by means of querying a command list from backend.

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton recheckTasksButton = new InlineKeyboardButton();
        InlineKeyboardButton start = new InlineKeyboardButton();
        InlineKeyboardButton getTask = new InlineKeyboardButton();
        InlineKeyboardButton help = new InlineKeyboardButton();

        recheckTasksButton.setText("Recheck tasks");
        recheckTasksButton.setCallbackData("recheck_tasks");
        start.setText("Start bot");
        start.setCallbackData("start");
        getTask.setText("Get random task");
        getTask.setCallbackData("get_random_task");
        help.setText("Help");
        help.setCallbackData("help");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        keyboardButtonsRow1.add(recheckTasksButton);
        keyboardButtonsRow2.add(start);
        keyboardButtonsRow3.add(getTask);
        keyboardButtonsRow4.add(help);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage keyboardMessage = new SendMessage();
        keyboardMessage.setChatId(chatId);
        keyboardMessage.setText("Menu:");
        keyboardMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(keyboardMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
