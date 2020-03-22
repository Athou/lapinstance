import { CharacterSpec, RaidSubscriptionResponse, RaidType } from "."

export const RaidTypes = Object.keys(RaidType) as RaidType[]
export const raidTypeLabels = {
    [RaidType.ONYXIA]: "Onyxia",
    [RaidType.MOLTEN_CORE]: "Molten Core",
    [RaidType.BLACKWING_LAIR]: "Blackwing Lair",
    [RaidType.AHN_QIRAJ_40]: "Ahn'Qiraj 40",
    [RaidType.AHN_QIRAJ_20]: "Ahn'Qiraj 20",
    [RaidType.ZUL_GURUB]: "Zul'Gurub",
    [RaidType.NAXXRAMAS]: "Naxxramas",
    [RaidType.PVP]: "PvP",
    [RaidType.OTHER]: "Autre"
}
export const raidTypeShortLabels = {
    [RaidType.ONYXIA]: "Ony",
    [RaidType.MOLTEN_CORE]: "MC",
    [RaidType.BLACKWING_LAIR]: "BWL",
    [RaidType.AHN_QIRAJ_40]: "AQ40",
    [RaidType.AHN_QIRAJ_20]: "AQ20",
    [RaidType.ZUL_GURUB]: "ZG",
    [RaidType.NAXXRAMAS]: "Naxx",
    [RaidType.PVP]: "PvP",
    [RaidType.OTHER]: "Autre"
}

export const characterSpecLabels = {
    [CharacterSpec.DRUID_RESTO]: "Druide - Restauration",
    [CharacterSpec.DRUID_BALANCE]: "Druide - Balance",
    [CharacterSpec.DRUID_TANK]: "Druide - Ours",
    [CharacterSpec.DRUID_CAT]: "Druide - Chat",
    [CharacterSpec.WARRIOR_TANK]: "Guerrier - Protection",
    [CharacterSpec.WARRIOR_DPS]: "Guerrier - Fureur",
    [CharacterSpec.PALADIN_PROT]: "Paladin  - Protection",
    [CharacterSpec.PALADIN_HEAL]: "Paladin - Sacré",
    [CharacterSpec.PALADIN_RET]: "Paladin - Vindicte",
    [CharacterSpec.MAGE]: "Mage",
    [CharacterSpec.ROGUE]: "Voleur",
    [CharacterSpec.WARLOCK]: "Démoniste",
    [CharacterSpec.HUNTER]: "Chasseur",
    [CharacterSpec.PRIEST_HEAL]: "Prêtre - Sacré",
    [CharacterSpec.PRIEST_SHADOW]: "Prêtre - Ombre"
}
export const CharacterSpecs = (Object.keys(CharacterSpec) as CharacterSpec[]).sort((a, b) =>
    characterSpecLabels[a].localeCompare(characterSpecLabels[b])
)

export const RaidSubscriptionResponses = Object.keys(RaidSubscriptionResponse) as RaidSubscriptionResponse[]
export const raidSubscriptionResponseLabels = {
    [RaidSubscriptionResponse.PRESENT]: "présent",
    [RaidSubscriptionResponse.LATE]: "en retard",
    [RaidSubscriptionResponse.BENCH]: "dispo si besoin",
    [RaidSubscriptionResponse.ABSENT]: "absent"
}

export enum CharacterRole {
    TANK = "TANK",
    MELEE = "MELEE",
    RANGED = "RANGED",
    HEAL = "HEAL"
}
export const characterRoleLabels = {
    [CharacterRole.TANK]: "Tank",
    [CharacterRole.MELEE]: "CAC",
    [CharacterRole.RANGED]: "Ranged",
    [CharacterRole.HEAL]: "Heal"
}
export const specToRoleMapping = {
    [CharacterSpec.DRUID_RESTO]: CharacterRole.HEAL,
    [CharacterSpec.DRUID_BALANCE]: CharacterRole.RANGED,
    [CharacterSpec.DRUID_TANK]: CharacterRole.TANK,
    [CharacterSpec.DRUID_CAT]: CharacterRole.MELEE,
    [CharacterSpec.WARRIOR_TANK]: CharacterRole.TANK,
    [CharacterSpec.WARRIOR_DPS]: CharacterRole.MELEE,
    [CharacterSpec.PALADIN_PROT]: CharacterRole.TANK,
    [CharacterSpec.PALADIN_HEAL]: CharacterRole.HEAL,
    [CharacterSpec.PALADIN_RET]: CharacterRole.MELEE,
    [CharacterSpec.MAGE]: CharacterRole.RANGED,
    [CharacterSpec.ROGUE]: CharacterRole.MELEE,
    [CharacterSpec.WARLOCK]: CharacterRole.RANGED,
    [CharacterSpec.HUNTER]: CharacterRole.RANGED,
    [CharacterSpec.PRIEST_HEAL]: CharacterRole.HEAL,
    [CharacterSpec.PRIEST_SHADOW]: CharacterRole.RANGED
}
