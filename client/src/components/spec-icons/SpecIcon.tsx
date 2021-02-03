import React from "react"
import styled from "styled-components"
import { CharacterSpec } from "../../api"
import druid_balance from "./druid_balance.png"
import druid_cat from "./druid_cat.png"
import druid_resto from "./druid_resto.png"
import druid_tank from "./druid_tank.png"
import hunter from "./hunter.png"
import mage from "./mage.png"
import paladin_heal from "./paladin_heal.png"
import paladin_prot from "./paladin_prot.png"
import paladin_ret from "./paladin_ret.png"
import priest_heal from "./priest_heal.png"
import priest_shadow from "./priest_shadow.png"
import rogue from "./rogue.png"
import shaman_elem from "./shaman_elem.jpg"
import shaman_enh from "./shaman_enh.jpg"
import shaman_resto from "./shaman_resto.jpg"
import warlock from "./warlock.png"
import warrior_dps from "./warrior_dps.png"
import warrior_tank from "./warrior_tank.png"

const specToIcon = {
    [CharacterSpec.DRUID_RESTO]: druid_resto,
    [CharacterSpec.DRUID_BALANCE]: druid_balance,
    [CharacterSpec.DRUID_TANK]: druid_tank,
    [CharacterSpec.DRUID_CAT]: druid_cat,
    [CharacterSpec.WARRIOR_TANK]: warrior_tank,
    [CharacterSpec.WARRIOR_DPS]: warrior_dps,
    [CharacterSpec.PALADIN_PROT]: paladin_prot,
    [CharacterSpec.PALADIN_HEAL]: paladin_heal,
    [CharacterSpec.PALADIN_RET]: paladin_ret,
    [CharacterSpec.ROGUE]: rogue,
    [CharacterSpec.MAGE]: mage,
    [CharacterSpec.WARLOCK]: warlock,
    [CharacterSpec.HUNTER]: hunter,
    [CharacterSpec.PRIEST_HEAL]: priest_heal,
    [CharacterSpec.PRIEST_SHADOW]: priest_shadow,
    [CharacterSpec.SHAMAN_RESTO]: shaman_resto,
    [CharacterSpec.SHAMAN_ENH]: shaman_enh,
    [CharacterSpec.SHAMAN_ELEM]: shaman_elem
}

const StyledImage = styled.img`
    vertical-align: middle;
`

export const SpecIcon: React.FC<{ spec: CharacterSpec; size?: number }> = props => {
    return <StyledImage src={specToIcon[props.spec]} height={props.size ?? 16} />
}
