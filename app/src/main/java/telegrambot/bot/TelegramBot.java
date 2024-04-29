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

                if (messageTextParts[0].substring(0, 1).equals("/")) {
                    commandExecuter(messageTextParts[0].substring(1));
                }
            }
        }

        if (update.hasCallbackQuery()) {
            String command = update.getCallbackQuery().getData();

            commandExecuter(command);
        }
    }

    private void commandExecuter(String command) {
        switch (command) {
        case "recheck-tasks":
            senderThread.interrupt();
            senderThread = new RandomSenderThread(this);
            senderThread.start();
            senderThread.setLastChatId(CHAT_ID);
            break;
        case "start":
            sendMessage(CHAT_ID, "Hello");
            break;
        case "get-task":
            sendRandomMedia(CHAT_ID);
            break;
        case "menu":
            generateMenuButtons(CHAT_ID);
            break;
        case "help":
            String help = "Commands: \n" + "/menu - push-button menu \n" + "/recheck-tasks - update bot tasks \n"
                    + "/start - start bot using \n" + "/get-task - request a new task \n"
                    + "/help - commands description";
            sendMessage(CHAT_ID, help);
            break;
        default:
            sendMessage(CHAT_ID, "Bad command");
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
        case "TEXT":
            sendMessage(CHAT_ID, backendTask.getContent());
            break;
        case "IMAGE":
            InputStream imgStream = new ByteArrayInputStream(
                    Base64.getDecoder().decode(backendTask.getContent().getBytes()));
            SendPhoto photoToSend = new SendPhoto();
            photoToSend.setPhoto(new InputFile(imgStream, "image.jpg"));

            sendPhoto(CHAT_ID, photoToSend);
            break;
        default:
            App.getLog().info("Media not parsed");
        }
    }

    private void generateMenuButtons(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton recheckTasksButton = new InlineKeyboardButton();
        InlineKeyboardButton start = new InlineKeyboardButton();
        InlineKeyboardButton getTask = new InlineKeyboardButton();
        InlineKeyboardButton help = new InlineKeyboardButton();

        recheckTasksButton.setText("Recheck tasks");
        recheckTasksButton.setCallbackData("recheck-tasks");
        start.setText("Start bot");
        start.setCallbackData("start");
        getTask.setText("Get task");
        getTask.setCallbackData("get-task");
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