package telegrambot.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Task schedule for telegram bot
 * 
 * @author Doomaykaka MIT License
 * @since 2024-05-01
 */
public class Schedule {
	/**
	 * List of tasks for telegram bot
	 */
	private List<String> schedule;

	/**
	 * Constructor that sets the list of tasks for the model
	 * 
	 * @param schedule
	 *            new list of tasks for telegram bot
	 */
	public Schedule(List<String> schedule) {
		super();
		this.schedule = schedule;
	}

	/**
	 * Method for getting a list of tasks
	 * 
	 * @return list of tasks for telegram bot
	 */
	public List<String> getSchedule() {
		return schedule;
	}

	/**
	 * Method for changing a list of tasks
	 * 
	 * @param schedule
	 *            new list of tasks for telegram bot
	 */
	public void setSchedule(List<String> schedule) {
		this.schedule = schedule;
	}

	/**
	 * Method for obtaining a string representation of a model's task schedule
	 * 
	 * @return string representation of a model's task schedule
	 */
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss z");

		StringBuilder sheduleRepresentation = new StringBuilder();

		sheduleRepresentation.append("Schedule :\n");
		for (String sheduleRecord : schedule) {
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(sheduleRecord);
			sheduleRepresentation.append(zonedDateTime.format(formatter));
			sheduleRepresentation.append(";\n");
		}

		return sheduleRepresentation.toString();
	}
}
