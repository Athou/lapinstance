import { CharacterSpec, RaidSubscriptionResponse, RaidType } from "."

export const raidTypeLabels = {
    [RaidType.ONYXIA]: "Onyxia",
    [RaidType.MOLTEN_CORE]: "Molten Core",
    [RaidType.BLACKWING_LAIR]: "Blackwing Lair",
    [RaidType.AHN_QIRAJ_40]: "Ahn'Qiraj 40",
    [RaidType.AHN_QIRAJ_20]: "Ahn'Qiraj 20",
    [RaidType.ZUL_GURUB]: "Zul'Gurub",
    [RaidType.NAXXRAMAS]: "Naxxramas",

    [RaidType.KARAZHAN]: "Karazhan",
    [RaidType.GRUUL]: "Gruul",
    [RaidType.MAGTHERIDON]: "Magtheridon",
    [RaidType.SERPENTSHRINE_CAVERN]: "Serpentshrine Cavern",
    [RaidType.TEMPEST_KEEP]: "Tempest Keep",
    [RaidType.HYJAL_SUMMIT]: "Hyjal Summit",
    [RaidType.BLACK_TEMPLE]: "Black Temple",
    [RaidType.ZUL_AMAN]: "Zul'Aman",
    [RaidType.SUNWELL_PLATEAU]: "Sunwell Plateau",

    [RaidType.VAULT_OF_ARCHAVON]: "Vault of Archavon",
    [RaidType.NAXXRAMAS_80]: "Naxxramas",
    [RaidType.THE_OBSIDIAN_SANCTUM]: "The Obsidian Sanctum",
    [RaidType.THE_EYE_OF_ETERNITY]: "The Eye of Eternity",
    [RaidType.ULDUAR]: "Ulduar",
    [RaidType.TRIAL_OF_THE_CRUSADER]: "Trial of the Crusader",
    [RaidType.ONYXIA_80]: "Onyxia",
    [RaidType.ICECROWN_CITADEL]: "Icecrown Citadel",

    [RaidType.PVP]: "PvP",
    [RaidType.OTHER]: "Autre",
}
export const raidTypeShortLabels = {
    [RaidType.ONYXIA]: "Ony",
    [RaidType.MOLTEN_CORE]: "MC",
    [RaidType.BLACKWING_LAIR]: "BWL",
    [RaidType.AHN_QIRAJ_40]: "AQ40",
    [RaidType.AHN_QIRAJ_20]: "AQ20",
    [RaidType.ZUL_GURUB]: "ZG",
    [RaidType.NAXXRAMAS]: "Naxx",

    [RaidType.KARAZHAN]: "KZ",
    [RaidType.GRUUL]: "Gruul",
    [RaidType.MAGTHERIDON]: "Mag",
    [RaidType.SERPENTSHRINE_CAVERN]: "SSC",
    [RaidType.TEMPEST_KEEP]: "TK",
    [RaidType.HYJAL_SUMMIT]: "Hyjal",
    [RaidType.BLACK_TEMPLE]: "BT",
    [RaidType.ZUL_AMAN]: "ZA",
    [RaidType.SUNWELL_PLATEAU]: "SWP",

    [RaidType.VAULT_OF_ARCHAVON]: "VoA",
    [RaidType.NAXXRAMAS_80]: "Naxx",
    [RaidType.THE_OBSIDIAN_SANCTUM]: "OS",
    [RaidType.THE_EYE_OF_ETERNITY]: "EoE",
    [RaidType.ULDUAR]: "Ulduar",
    [RaidType.TRIAL_OF_THE_CRUSADER]: "ToC",
    [RaidType.ONYXIA_80]: "Ony",
    [RaidType.ICECROWN_CITADEL]: "ICC",

    [RaidType.PVP]: "PvP",
    [RaidType.OTHER]: "Autre",
}

export type Expansion = "vanilla" | "tbc" | "wotlk" | "all"

export const expansionLabels: { [key in Expansion]: string } = {
    vanilla: "Vanilla",
    tbc: "The Burning Crusade",
    wotlk: "Wrath of the Lich King",
    all: "Tous",
}

export const raidTypeExpansions: { [key in Expansion]: RaidType[] } = {
    vanilla: [
        RaidType.ONYXIA,
        RaidType.MOLTEN_CORE,
        RaidType.BLACKWING_LAIR,
        RaidType.AHN_QIRAJ_40,
        RaidType.AHN_QIRAJ_20,
        RaidType.ZUL_GURUB,
        RaidType.NAXXRAMAS,
    ],
    tbc: [
        RaidType.KARAZHAN,
        RaidType.GRUUL,
        RaidType.MAGTHERIDON,
        RaidType.SERPENTSHRINE_CAVERN,
        RaidType.TEMPEST_KEEP,
        RaidType.HYJAL_SUMMIT,
        RaidType.BLACK_TEMPLE,
        RaidType.ZUL_AMAN,
        RaidType.SUNWELL_PLATEAU,
    ],
    wotlk: [
        RaidType.VAULT_OF_ARCHAVON,
        RaidType.NAXXRAMAS_80,
        RaidType.THE_OBSIDIAN_SANCTUM,
        RaidType.THE_EYE_OF_ETERNITY,
        RaidType.ULDUAR,
        RaidType.TRIAL_OF_THE_CRUSADER,
        RaidType.ONYXIA_80,
        RaidType.ICECROWN_CITADEL,
    ],

    all: [RaidType.PVP, RaidType.OTHER],
}
export const getExpansion = (raidType: RaidType) =>
    Object.keys(raidTypeExpansions).find(expansion => raidTypeExpansions[expansion as Expansion].includes(raidType)) as Expansion

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
    [CharacterSpec.PRIEST_SHADOW]: "Prêtre - Ombre",
    [CharacterSpec.SHAMAN_ENH]: "Chaman - Amélioration",
    [CharacterSpec.SHAMAN_ELEM]: "Chaman - Élémentaire",
    [CharacterSpec.SHAMAN_RESTO]: "Chaman - Restauration",
    [CharacterSpec.DEATH_KNIGHT_BLOOD]: "Chevalier de la mort - Sang",
    [CharacterSpec.DEATH_KNIGHT_FROST]: "Chevalier de la mort - Givre",
    [CharacterSpec.DEATH_KNIGHT_UNHOLY]: "Chevalier de la mort - Impie",
}
export const CharacterSpecs = (Object.keys(CharacterSpec) as CharacterSpec[]).sort((a, b) =>
    characterSpecLabels[a].localeCompare(characterSpecLabels[b])
)

export const RaidSubscriptionResponses = Object.keys(RaidSubscriptionResponse) as RaidSubscriptionResponse[]
export const raidSubscriptionResponseLabels = {
    [RaidSubscriptionResponse.PRESENT]: "présent",
    [RaidSubscriptionResponse.LATE]: "en retard",
    [RaidSubscriptionResponse.BENCH]: "dispo si besoin",
    [RaidSubscriptionResponse.ABSENT]: "absent",
}

export enum CharacterRole {
    TANK = "TANK",
    MELEE = "MELEE",
    RANGED = "RANGED",
    HEAL = "HEAL",
}
export const characterRoleLabels = {
    [CharacterRole.TANK]: "Tank",
    [CharacterRole.MELEE]: "CAC",
    [CharacterRole.RANGED]: "Ranged",
    [CharacterRole.HEAL]: "Heal",
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
    [CharacterSpec.PRIEST_SHADOW]: CharacterRole.RANGED,
    [CharacterSpec.SHAMAN_ENH]: CharacterRole.MELEE,
    [CharacterSpec.SHAMAN_ELEM]: CharacterRole.RANGED,
    [CharacterSpec.SHAMAN_RESTO]: CharacterRole.HEAL,
    [CharacterSpec.DEATH_KNIGHT_BLOOD]: CharacterRole.TANK,
    [CharacterSpec.DEATH_KNIGHT_FROST]: CharacterRole.MELEE,
    [CharacterSpec.DEATH_KNIGHT_UNHOLY]: CharacterRole.MELEE,
}
