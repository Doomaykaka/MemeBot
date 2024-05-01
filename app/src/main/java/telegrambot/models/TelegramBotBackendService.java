package telegrambot.models;

import java.util.List;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TelegramBotBackendService {
	@GET("/task")
	Call<Task> getTask(@Header("authorization") String token);

	@GET("/scheduler/next")
	Call<ResponseBody> getNextTaskTime(@Header("authorization") String token);

	@POST("/login")
	@Headers("Content-Type: application/json")
	Call<LoginResult> login(@Body RequestBody params);

	@GET("/scheduler/update")
	@Headers({"Content-Type: application/json", "accept: application/json"})
	Call<List<String>> getSheduleUpdate(@Header("authorization") String token, @Query("schedule") String shedule);
}
