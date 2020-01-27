import { Checkbox, HTMLTable } from "@blueprintjs/core"
import React from "react"
import styled from "styled-components"
import { RaidType, RosterMember, UserCharacter } from "../../api"
import { raidTypeLabels, RaidTypes } from "../../api/utils"

const StyledTable = styled(HTMLTable)`
    width: 100%;
`

const CenteredCell = styled.td`
    text-align: center !important;
`

export const RosterTable: React.FC<{
    characters: UserCharacter[]
    roster: RosterMember[]
    onSelect: (character: UserCharacter, raidType: RaidType) => void
    onDeselect: (character: UserCharacter, raidType: RaidType) => void
}> = props => {
    const isMember = (character: UserCharacter, raidType: RaidType) => {
        return props.roster.some(rm => rm.raidType === raidType && rm.userCharacter.id === character.id)
    }

    return (
        <StyledTable condensed interactive>
            <thead>
                <tr>
                    <th></th>
                    {RaidTypes.map(type => (
                        <CenteredCell key={type}>{raidTypeLabels[type]}</CenteredCell>
                    ))}
                </tr>
            </thead>
            <tbody>
                {props.characters.map(char => (
                    <tr key={char.id}>
                        <td>
                            {char.name}
                            {!char.main && " (r)"}
                        </td>
                        {RaidTypes.map(type => (
                            <CenteredCell key={type}>
                                <Checkbox
                                    checked={isMember(char, type)}
                                    onClick={() => (isMember(char, type) ? props.onDeselect : props.onSelect)(char, type)}
                                />
                            </CenteredCell>
                        ))}
                    </tr>
                ))}
            </tbody>
        </StyledTable>
    )
}
