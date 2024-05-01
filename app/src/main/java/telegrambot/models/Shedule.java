package telegrambot.models;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Shedule {
	private List<String> shedule;

	public Shedule(List<String> shedule) {
		super();
		this.shedule = shedule;
	}

	public List<String> getShedule() {
		return shedule;
	}

	public void setShedule(List<String> shedule) {
		this.shedule = shedule;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss z");

		StringBuilder sheduleRepresentation = new StringBuilder();

		sheduleRepresentation.append("Shedule :\n");
		for (String sheduleRecord : shedule) {
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(sheduleRecord);
			sheduleRepresentation.append(zonedDateTime.format(formatter));
			sheduleRepresentation.append(";\n");
		}

		return sheduleRepresentation.toString();
	}
}
