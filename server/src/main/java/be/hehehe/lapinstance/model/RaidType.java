package be.hehehe.lapinstance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All instance available to raid.
 */
@Getter
@AllArgsConstructor
public enum RaidType {

	ONYXIA("Ony", "Onyxia", 5),
	MOLTEN_CORE("MC", "Molten Core", 7),
	ZUL_GURUB("ZG", "Zul'Gurub", 3),
	BLACKWING_LAIR("BWL", "Blackwing Lair", 7),
	AHN_QIRAJ_20("AQ20", "Ahn'Qiraj 20", 3),
	AHN_QIRAJ_40("AQ40", "Ahn'Qiraj 40", 7),
	NAXXRAMAS("Naxx", "Naxxramas", 7),

	PVP("PvP", "PvP", -1),
	OTHER("Autre", "Autre", -1);

	private final String shortName;
	private final String longName;
	private final int resetTimer;

}
