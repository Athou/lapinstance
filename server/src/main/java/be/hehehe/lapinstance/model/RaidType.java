package be.hehehe.lapinstance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All instance available to raid.
 */
@Getter
@AllArgsConstructor
public enum RaidType {

	ONYXIA("Ony", "Onyxia", 40, 5),
	MOLTEN_CORE("MC", "Molten Core", 40, 7),
	ZUL_GURUB("ZG", "Zul'Gurub", 20, 7),
	BLACKWING_LAIR("BWL", "Blackwing Lair", 40, 7),
	AHN_QIRAJ_20("AQ20", "Ahn'Qiraj 20", 20, 3),
	AHN_QIRAJ_40("AQ40", "Ahn'Qiraj 40", 40, 7),
	NAXXRAMAS("Naxx", "Naxxramas", 40, 7);

	private final String shortName;
	private final String longName;
	private final int numberOfPlayers;
	private final int resetTimer;

}
