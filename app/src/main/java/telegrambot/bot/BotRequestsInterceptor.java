package telegrambot.bot;

import java.io.IOException;
import java.net.HttpURLConnection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Request Interceptor
 *
 * @see BotInitializer
 * @see BotRequests
 * @author Doomaykaka MIT License
 * @since 2024-05-26
 */
public class BotRequestsInterceptor implements Interceptor {

    /**
     * Method that fires when a request is executed
     *
     * @param chain
     *            Connection object
     * @return A method that makes a request to get a task that the bot needs to
     *         perform
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            BotInitializer.setToken("Bearer " + BotRequests.sendLoginRequest());
        }

        return response;
    }

}
