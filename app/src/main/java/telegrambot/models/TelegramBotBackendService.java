package telegrambot.models;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TelegramBotBackendService {
    @GET("/media")
    Call<ResponseBody> getMedia(@Query("as_attachment") boolean asAttachment);

    @GET("/scheduler/next")
    Call<ResponseBody> getNextSendPhotoTime();
}
