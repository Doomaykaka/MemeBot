package telegrambot.bot;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import telegrambot.models.TelegramBotBackendService;

public class BotRequests {
    public static SendPhoto getRandomPhoto() {
        SendPhoto responseMessage = null;

        boolean asAttachment = true;

        TelegramBotBackendService botService = BotInitializer.getBackendHttpClientService();
        Call<ResponseBody> response = botService.getMedia(asAttachment);

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
        Call<ResponseBody> response = botService.getNextSendPhotoTime();

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
}
