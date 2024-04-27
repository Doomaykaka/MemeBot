package telegrambot.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import telegrambot.models.TelegramBotBackendService;

public class BotInitializer {
    private static TelegramBotBackendService backendHttpClientService;
    
    public static void initialize() {
        initializeBackendHttpClient();
        createBot();
    }
    
    private static void initializeBackendHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Retrofit retrofitClient = new Retrofit
                                      .Builder()
                                      .client(httpClient)
                                      .baseUrl("https://memebot.v01d.ru/")
                                      .build();
        
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
