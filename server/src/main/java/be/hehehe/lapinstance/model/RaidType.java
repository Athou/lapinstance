package be.hehehe.lapinstance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All instance available to raid.
 */
@Getter
@AllArgsConstructor
public enum RaidType {

	ONYXIA("Ony", "Onyxia"),
	MOLTEN_CORE("MC", "Molten Core"),
	ZUL_GURUB("ZG", "Zul'Gurub"),
	BLACKWING_LAIR("BWL", "Blackwing Lair"),
	AHN_QIRAJ_20("AQ20", "Ahn'Qiraj 20"),
	AHN_QIRAJ_40("AQ40", "Ahn'Qiraj 40"),
	NAXXRAMAS("Naxx", "Naxxramas"),

	PVP("PvP", "PvP"),
	OTHER("Autre", "Autre");

	private final String shortName;
	private final String longName;

}
