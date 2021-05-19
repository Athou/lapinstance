package be.hehehe.lapinstance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RaidResetDuration {
	THREE_DAYS(3), FIVE_DAYS(5), SEVEN_DAYS(7);

	private int days;
}
