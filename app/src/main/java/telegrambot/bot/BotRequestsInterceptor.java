package telegrambot.bot;

import java.io.IOException;
import java.net.HttpURLConnection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BotRequestsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            BotInitializer.setToken("Bearer " + BotRequests.sendLoginRequest());

            response = chain.proceed(request);
        }

        return response;
    }

}
