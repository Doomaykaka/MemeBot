package telegrambot.bot;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.simple.JSONObject;
import retrofit2.Call;
import retrofit2.Response;
import telegrambot.App;
import telegrambot.models.LoginResult;
import telegrambot.models.Task;
import telegrambot.models.TelegramBotBackendService;

/**
 * Set of requests to the server
 *
 * @see Task
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class BotRequests {
    /**
     * A method that makes a request to get a task that the bot needs to perform
     *
     * @return A method that makes a request to get a task that the bot needs to
     *         perform
     */
    public static Task getTaskByCommand(String command) {
        Task task = null;

        TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
        Call<Task> response = botService.getTaskByCommand(BotInitializer.getToken(), command);

        Response<Task> responseBody = null;
        try {
            responseBody = response.execute();

            if (responseBody == null) {
                throw new IOException("Null response");
            }

            task = ((Task) responseBody.body());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return task;

    }

    /**
     * Method that performs a request to get the time to start next task
     *
     * @return time to start next task
     */
    public static ZonedDateTime sendNextSendTaskTimeRequest() {
        ZonedDateTime nextSendTime = null;

        TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
        Call<ResponseBody> response = botService.getNextTaskTime(BotInitializer.getToken());

        String time;

        Response<ResponseBody> responseBody = null;
        try {
            responseBody = response.execute();

            if (responseBody != null) {
                time = responseBody.body().string();

                nextSendTime = ZonedDateTime.parse(time);
            }

        } catch (IOException | DateTimeParseException e) {
            e.printStackTrace();
        }

        return nextSendTime;
    }

    /**
     * Method that performs an authorization request on the server
     *
     * @return token for authorization on the server
     */
    public static String sendLoginRequest() {
        String token = null;

        JSONObject postParams = new JSONObject();
        postParams.put("login", App.getBotConfig().backendLogin);
        postParams.put("password", App.getBotConfig().backendPassword);

        String JSON = postParams.toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON);

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
