package telegrambot.models;

/**
 * Result of authorization on the server
 *
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class LoginResult {
    /**
     * A token that allows authorized users to perform requests
     */
    private String token;

    /**
     * Constructor that sets the Telegram bot authorization token
     *
     * @param token
     *            telegram bot authorization token
     */
    public LoginResult(String token) {
        this.token = token;
    }

    /**
     * Method for obtaining an authorization token
     *
     * @return authorization token
     */
    public String getToken() {
        return token;
    }

    /**
     * Method for changing authorization token
     *
     * @param token
     *            new auth token value
     */
    public void setToken(String token) {
        this.token = token;
    }
}
