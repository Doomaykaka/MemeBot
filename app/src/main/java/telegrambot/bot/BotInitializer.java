package telegrambot.bot;

import okhttp3.OkHttpClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import retrofit2.Retrofit;
import telegrambot.App;
import telegrambot.models.TelegramBotBackendService;

public class BotInitializer {
    private static TelegramBotBackendService backendHttpClientService;

    public static void initialize() {
        initializeBackendHttpClient();
        createBot();
    }

    private static void initializeBackendHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String botBackendURL = App.getBotConfig().getBotBackendUrl();
        Retrofit retrofitClient = new Retrofit.Builder().client(httpClient).baseUrl(botBackendURL).build();

        backendHttpClientService = retrofitClient.create(TelegramBotBackendService.class);
    }

    private static void createBot() {
        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static TelegramBotBackendService getBackendHttpClientService() {
        return backendHttpClientService;
    }
}
