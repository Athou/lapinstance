package be.hehehe.lapinstance.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The possible RSVP response available to a {@link Raid}.
 */
@RequiredArgsConstructor
@Getter
public enum RaidSubscriptionResponse {
	PRESENT("Inscrit"), LATE("En retard"), BENCH("Bench"), ABSENT("Absent");

	private final String name;
}
