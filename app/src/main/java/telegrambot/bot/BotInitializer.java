package telegrambot.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import telegrambot.App;
import telegrambot.config.Config;
import telegrambot.models.TelegramBotBackendService;

/**
 * An initializer that creates an HTTP client, authorization on the server, as
 * well as creating and launching a telegram bot
 *
 * @see TelegramBotBackendService
 * @see App
 * @see TelegramBot
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class BotInitializer {
	/**
	 * Service containing a list of available HTTP requests to the server
	 */
	private static TelegramBotBackendService backendHttpClientService;
	/**
	 * Token for authorization on the server
	 */
	private static String token;

	/**
	 * The method creates an HTTP client, performs authorization on the server and
	 * creates a telegram bot
	 *
	 * @throws TelegramApiException
	 */
	public static void initialize() throws TelegramApiException {
		App.getLog().info("HTTP client initialization");
		initializeBackendHttpClient();

		App.getLog().info("Getting token");
		login();

		App.getLog().info("Creating telegram bot");
		createBot();
	}

	/**
	 * The method creates an HTTP client
	 */
	private static void initializeBackendHttpClient() {
		Gson gson = new GsonBuilder().setLenient().create();

		// TODO: @Doomaykaka, перепиши, пожалуйста, как тебе нравится.
		// Взял код тут:
		// https://stackoverflow.com/questions/37162055/how-to-set-timeout-in-retrofit-2-0-android
		// да, я понимаю, что этим цифрам место в конфиге, но был нужен быстрый фикс.
		// Хорошо, что у меня 10 лет
		// опыта написания проектов на джаве и доступ в гугл.
		// P.S. Возможно, споттлес заменяет пробелы на табы.

		OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
				.writeTimeout(20, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
		String botBackendURL = App.getBotConfig().botBackendUrl;
		Retrofit retrofitClient = new Retrofit.Builder().client(httpClient).baseUrl(botBackendURL)
				.addConverterFactory(GsonConverterFactory.create(gson)).build();

		backendHttpClientService = retrofitClient.create(TelegramBotBackendService.class);
	}

	/**
	 * The method performs authorization on the server
	 */
	private static void login() {
		token = "Bearer " + BotRequests.sendLoginRequest();
	}

	/**
	 * The method creates a telegram bot
	 */
	private static void createBot() throws TelegramApiException {
		TelegramBotsApi botsApi;
		botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(new TelegramBot());
	}

	/**
	 * A method for obtaining a service containing a list of available HTTP requests
	 * to the server
	 *
	 * @return service containing a list of available HTTP requests to the server
	 */
	public static TelegramBotBackendService getBackendHttpClientService() {
		return backendHttpClientService;
	}

	/**
	 * Method for obtaining a token for authorization on the server
	 *
	 * @return token for authorization on the server
	 */
	public static String getToken() {
		return token;
	}
}
