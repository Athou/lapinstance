package be.hehehe.lapinstance.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CharacterClass {
	WARRIOR("Guerrier"),
	PALADIN("Paladin"),
	DRUID("Druide"),
	PRIEST("Prêtre"),
	ROGUE("Voleur"),
	MAGE("Mage"),
	SHAMAN("Chaman"),
	WARLOCK("Démoniste"),
	HUNTER("Chasseur"),
	DEATH_KNIGHT("Chevalier de la mort");

	private final String name;
}
