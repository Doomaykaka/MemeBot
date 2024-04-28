package telegrambot.bot;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.simple.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import retrofit2.Call;
import retrofit2.Response;
import telegrambot.App;
import telegrambot.models.LoginResult;
import telegrambot.models.TelegramBotBackendService;

public class BotRequests {

	public static SendPhoto getRandomPhoto() {
		SendPhoto responseMessage = null;

		boolean asAttachment = true;

		TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
		Call<ResponseBody> response = botService.getMedia(asAttachment, BotInitializer.getToken());

		Response<ResponseBody> responseBody = null;
		try {
			responseBody = response.execute();

			if (responseBody != null) {
				String contentType = responseBody.headers().get("Content-Type");
				String mediaFilePreffix = contentType.substring(contentType.indexOf("/") + 1, contentType.length());

				InputStream dataInputStream = responseBody.body().byteStream();

				responseMessage = new SendPhoto();
				responseMessage.setPhoto(new InputFile(dataInputStream, "media." + mediaFilePreffix));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseMessage;

	}

	public static ZonedDateTime sendNextSendPhotoTimeRequest() {
		ZonedDateTime nextSendTime = null;

		TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
		Call<ResponseBody> response = botService.getNextSendPhotoTime(BotInitializer.getToken());

		String time;

		Response<ResponseBody> responseBody = null;
		try {
			responseBody = response.execute();

			if (responseBody != null) {
				time = responseBody.body().string();

				nextSendTime = ZonedDateTime.parse(time);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return nextSendTime;
	}

	public static String sendLoginRequest() {
		String token = null;

		JSONObject postParams = new JSONObject();
		postParams.put("login", App.getBotConfig().getBackendLogin());
		postParams.put("password", App.getBotConfig().getBackendPassword());

		String JSON = postParams.toString();

		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (JSON));

		TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
		Call<LoginResult> response = botService.login(requestBody);

		Response<LoginResult> responseBody = null;
		try {
			responseBody = response.execute();

			if (responseBody != null) {
				LoginResult loginResult = ((LoginResult) responseBody.body());
				token = loginResult.getToken();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return token;
	}
}
