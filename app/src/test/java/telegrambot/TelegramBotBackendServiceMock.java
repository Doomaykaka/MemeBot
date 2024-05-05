package telegrambot;

import java.io.IOException;
import java.time.ZonedDateTime;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import telegrambot.models.LoginResult;
import telegrambot.models.Task;
import telegrambot.models.TelegramBotBackendService;

public class TelegramBotBackendServiceMock implements TelegramBotBackendService {

    private boolean isCorrect = true;

    @Override
    public Call<Task> getTaskByCommand(String token, String command) {
        CallMock<Task> callable = new CallMock<Task>();
        callable.setTestType(TestType.TASK);
        callable.setCorrect(isCorrect);

        return callable;
    }

    @Override
    public Call<ResponseBody> getNextTaskTime(String token) {
        CallMock<ResponseBody> callable = new CallMock<ResponseBody>();
        callable.setTestType(TestType.RESPONSE);
        callable.setCorrect(isCorrect);
        return callable;
    }

    @Override
    public Call<LoginResult> login(RequestBody params) {
        CallMock<LoginResult> callable = new CallMock<LoginResult>();
        callable.setTestType(TestType.LOGIN);
        callable.setCorrect(isCorrect);
        return callable;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public class CallMock<T> implements Call<T> {

        private TestType testType;

        private boolean isCorrect = true;

        private final String TASK_CORRECT_ANSWER = "Hello";

        private final String TASK_UNCORRECT_ANSWER = null;

        private final String LOGIN_CORRECT_ANSWER = "123";

        private final String LOGIN_UNCORRECT_ANSWER = null;

        private final String NEXT_TIME_CORRECT_ANSWER = ZonedDateTime.now().toString();

        private final String NEXT_TIME_UNCORRECT_ANSWER = "";

        @Override
        public Response<T> execute() throws IOException {
            if (testType.equals(TestType.TASK)) {
                Task task;

                if (isCorrect) {
                    task = new Task(TASK_CORRECT_ANSWER, null);
                } else {
                    task = new Task(TASK_UNCORRECT_ANSWER, null);
                }

                return (Response<T>) Response.success(task);
            }

            if (testType.equals(TestType.RESPONSE)) {

                String time;

                if (isCorrect) {
                    time = NEXT_TIME_CORRECT_ANSWER;
                } else {
                    time = NEXT_TIME_UNCORRECT_ANSWER;
                }

                ResponseBody timeResponse = ResponseBody.create(MediaType.parse("text/plain"), time);

                return (Response<T>) Response.success(timeResponse);
            }

            if (testType.equals(TestType.LOGIN)) {
                LoginResult login;

                if (isCorrect) {
                    login = new LoginResult(LOGIN_CORRECT_ANSWER);
                } else {
                    login = new LoginResult(LOGIN_UNCORRECT_ANSWER);
                }

                return (Response<T>) Response.success(login);
            }

            return null;
        }

        @Override
        public void enqueue(Callback<T> callback) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isExecuted() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void cancel() {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isCanceled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public Request request() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Call<T> clone() {
            return null;
        }

        public boolean isCorrect() {
            return isCorrect;
        }

        public void setCorrect(boolean isCorrect) {
            this.isCorrect = isCorrect;
        }

        public TestType getTestType() {
            return testType;
        }

        public void setTestType(TestType testType) {
            this.testType = testType;
        }

    }

    public enum TestType {
        LOGIN, RESPONSE, TASK
    }
}
