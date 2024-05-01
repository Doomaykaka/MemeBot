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
    @GET("/task")
    Call<Task> getTask(@Header("authorization") String token);

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

    /**
     * Method that performs a request to update the task execution schedule
     *
     * @param token
     *            authorization token
     * @param schedule
     *            schedule according to which tasks will be executed
     * @return Callable list of strings
     */
    @GET("/scheduler/update")
    @Headers({"Content-Type: application/json", "accept: application/json"})
    Call<List<String>> getSheduleUpdate(@Header("authorization") String token, @Query("schedule") String schedule);

    /**
     * A method that performs a request to update the topic of the received content.
     *
     * @param token
     *            authorization token
     * @param theme
     *            search theme
     * @return updated theme
     */
    @GET("/users/update_theme")
    Call<String> getThemeUpdate(@Header("authorization") String token, @Query("theme") String theme);
}
