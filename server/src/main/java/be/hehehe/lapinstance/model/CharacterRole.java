package be.hehehe.lapinstance.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The possible role of a {@link CharacterSpec}
 */
@Getter
@RequiredArgsConstructor
public enum CharacterRole {
	HEAL("Heal"), TANK("Tank"), DPS_RANGED("Ranged"), DPS_CAC("CAC");

	private final String name;
}
