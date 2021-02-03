package be.hehehe.lapinstance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The class and spec of a {@link UserCharacter}.
 */
@Getter
@AllArgsConstructor
public enum CharacterSpec {
	WARRIOR_TANK(CharacterClass.WARRIOR, CharacterRole.TANK),
	PALADIN_PROT(CharacterClass.PALADIN, CharacterRole.TANK),
	DRUID_TANK(CharacterClass.DRUID, CharacterRole.TANK),
	PRIEST_HEAL(CharacterClass.PRIEST, CharacterRole.HEAL),
	PALADIN_HEAL(CharacterClass.PALADIN, CharacterRole.HEAL),
	DRUID_RESTO(CharacterClass.DRUID, CharacterRole.HEAL),
	SHAMAN_RESTO(CharacterClass.SHAMAN, CharacterRole.HEAL),
	ROGUE(CharacterClass.ROGUE, CharacterRole.DPS_CAC),
	WARRIOR_DPS(CharacterClass.WARRIOR, CharacterRole.DPS_CAC),
	PALADIN_RET(CharacterClass.PALADIN, CharacterRole.DPS_CAC),
	DRUID_CAT(CharacterClass.DRUID, CharacterRole.DPS_CAC),
	SHAMAN_ENH(CharacterClass.SHAMAN, CharacterRole.DPS_CAC),
	MAGE(CharacterClass.MAGE, CharacterRole.DPS_RANGED),
	WARLOCK(CharacterClass.WARLOCK, CharacterRole.DPS_RANGED),
	HUNTER(CharacterClass.HUNTER, CharacterRole.DPS_RANGED),
	PRIEST_SHADOW(CharacterClass.PRIEST, CharacterRole.DPS_RANGED),
	DRUID_BALANCE(CharacterClass.DRUID, CharacterRole.DPS_RANGED),
	SHAMAN_ELEM(CharacterClass.SHAMAN, CharacterRole.DPS_RANGED);

	private final CharacterClass characterClass;
	private final CharacterRole role;

}
