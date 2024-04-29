package telegrambot.models;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface TelegramBotBackendService {
	@GET("/task")
	Call<Task> getTask(@Header("authorization") String token);

	@GET("/scheduler/next")
	Call<ResponseBody> getNextTaskTime(@Header("authorization") String token);

	@POST("/login")
	@Headers("Content-Type: application/json")
	Call<LoginResult> login(@Body RequestBody params);
}
