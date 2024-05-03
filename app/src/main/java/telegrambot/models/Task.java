package telegrambot.models;

/**
 * Task for telegram bot
 *
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class Task {
    /**
     * Text to be processed
     */
    private String text;
    /**
     * Image to be processed
     */
    private String image;

    /**
     * Constructor that sets the type and content to be processed
     *
     * @param text
     *            text to be processed
     * @param image
     *            image to be processed
     */
    public Task(String text, String image) {
        super();
        this.text = text;
        this.image = image;
    }

    /**
     * Method to get text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Method for changing text
     *
     * @param text
     *            new text value
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Method to get image
     *
     * @return image
     */
    public String getImage() {
        return image;
    }

    /**
     * Method for changing image
     *
     * @param image
     *            new image value
     */
    public void setImage(String image) {
        this.image = image;
    }
}
