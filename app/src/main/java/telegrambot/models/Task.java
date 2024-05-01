package telegrambot.models;

/**
 * Task for telegram bot
 * 
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class Task {
	/**
	 * Type of content to be processed
	 */
	private String type;
	/**
	 * Content that needs to be processed
	 */
	private String content;

	/**
	 * Constructor that sets the type and content to be processed
	 * 
	 * @param type
	 *            type to be processed
	 * @param content
	 *            content to be processed
	 */
	public Task(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

	/**
	 * Method to get content type
	 * 
	 * @return content type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Method for changing content type
	 * 
	 * @param type
	 *            new content type value
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Method to get content
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Method for changing content
	 * 
	 * @param content
	 *            new content value
	 */
	public void setContent(String content) {
		this.content = content;
	}
}
