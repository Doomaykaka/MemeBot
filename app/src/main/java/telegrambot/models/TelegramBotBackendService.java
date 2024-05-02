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

/**
 * Description of the set of requests to the server
 *
 * @see Task
 * @see LoginResult
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public interface TelegramBotBackendService {
    /**
     * Method executing a request to receive a task related to mailing
     *
     * @param token
     *            authorization token
     * @return callable task
     */
    @GET("/execute")
    Call<Task> getTaskByCommand(@Header("authorization") String token, @Query("command") String command);

    /**
     * Method that performs a request to get the time to start next task
     *
     * @param token
     *            authorization token
     * @return callable response body
     */
    @GET("/scheduler/next")
    Call<ResponseBody> getNextTaskTime(@Header("authorization") String token);

    /**
     * Method that performs an authorization request on the server
     *
     * @param params
     *            authorization request body
     * @return callable login result
     */
    @POST("/login")
    @Headers("Content-Type: application/json")
    Call<LoginResult> login(@Body RequestBody params);
}
