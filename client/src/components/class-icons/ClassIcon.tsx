import React from "react"
import styled from "styled-components"
import { CharacterClass } from "../../api"
import druid from "./druid.png"
import hunter from "./hunter.png"
import mage from "./mage.png"
import paladin from "./paladin.png"
import priest from "./priest.png"
import rogue from "./rogue.png"
import shaman from "./shaman.png"
import warlock from "./warlock.png"
import warrior from "./warrior.png"

const classToIcon = {
    [CharacterClass.DRUID]: druid,
    [CharacterClass.WARRIOR]: warrior,
    [CharacterClass.PALADIN]: paladin,
    [CharacterClass.MAGE]: mage,
    [CharacterClass.ROGUE]: rogue,
    [CharacterClass.WARLOCK]: warlock,
    [CharacterClass.HUNTER]: hunter,
    [CharacterClass.PRIEST]: priest,
    [CharacterClass.SHAMAN]: shaman
}

const StyledImage = styled.img`
    vertical-align: middle;
`

export const ClassIcon: React.FC<{ characterClass: CharacterClass; size?: number }> = props => {
    return <StyledImage src={classToIcon[props.characterClass]} height={props.size ?? 16} />
}
