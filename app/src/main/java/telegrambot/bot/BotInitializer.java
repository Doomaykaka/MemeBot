package telegrambot.bot;

import okhttp3.OkHttpClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import telegrambot.App;
import telegrambot.models.TelegramBotBackendService;

public class BotInitializer {
	private static TelegramBotBackendService backendHttpClientService;
	private static String token;

	public static void initialize() {
		initializeBackendHttpClient();
		login();
		createBot();
	}

	private static void initializeBackendHttpClient() {
		OkHttpClient httpClient = new OkHttpClient.Builder().build();
		String botBackendURL = App.getBotConfig().getBotBackendUrl();
		Retrofit retrofitClient = new Retrofit.Builder().client(httpClient).baseUrl(botBackendURL)
				.addConverterFactory(GsonConverterFactory.create()).build();

		backendHttpClientService = retrofitClient.create(TelegramBotBackendService.class);
	}

	private static void login() {
		token = "Bearer " + BotRequests.sendLoginRequest();
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

	public static String getToken() {
		return token;
	}
}
