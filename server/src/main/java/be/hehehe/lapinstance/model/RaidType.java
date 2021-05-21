package be.hehehe.lapinstance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All instance available to raid.
 */
@Getter
@AllArgsConstructor
public enum RaidType {

	// vanilla
	ONYXIA("Onyxia"),
	MOLTEN_CORE("Molten Core"),
	ZUL_GURUB("Zul'Gurub"),
	BLACKWING_LAIR("Blackwing Lair"),
	AHN_QIRAJ_20("Ahn'Qiraj 20"),
	AHN_QIRAJ_40("Ahn'Qiraj 40"),
	NAXXRAMAS("Naxxramas"),

	// tbc
	KARAZHAN("Karazhan"),
	GRUUL("Gruul"),
	MAGTHERIDON("Magtheridon"),
	SERPENTSHRINE_CAVERN("Serpentshrine Cavern"),
	TEMPEST_KEEP("Tempest Keep"),
	HYJAL_SUMMIT("Hyjal Summit"),
	BLACK_TEMPLE("Black Temple"),
	ZUL_AMAN("Zul'Aman"),
	SUNWELL_PLATEAU("Sunwell Plateau"),

	PVP("PvP"),
	OTHER("Autre");

	private final String name;

}
