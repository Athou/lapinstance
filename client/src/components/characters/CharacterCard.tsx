import { Button, Card, Elevation, H5, Icon } from "@blueprintjs/core"
import React from "react"
import styled from "styled-components"
import { UserCharacter } from "../../api"
import { characterSpecLabels } from "../../api/utils"
import { SpecIcon } from "../spec-icons/SpecIcon"

const CardTitle = styled(H5)`
    display: flex;
`

const CardTitleText = styled.div`
    flex-grow: 1;
`

const StyledCharacterSpec = styled.span`
    margin-left: 0.2rem;
`

const StyledMainIcon = styled(Icon)`
    margin-right: 0.4rem;
`

export const CharacterCard: React.FC<{ character: UserCharacter; editable: boolean; onEdit: () => void }> = props => {
    return (
        <>
            <Card elevation={Elevation.TWO}>
                <CardTitle>
                    {props.character.main && <StyledMainIcon icon="tick-circle" iconSize={18} />}
                    <CardTitleText>{props.character.name}</CardTitleText>
                    {props.editable && <Button icon="edit" onClick={props.onEdit} />}
                </CardTitle>
                <div>
                    <SpecIcon spec={props.character.spec} />
                    <StyledCharacterSpec>{characterSpecLabels[props.character.spec]}</StyledCharacterSpec>
                </div>
            </Card>
        </>
    )
}
