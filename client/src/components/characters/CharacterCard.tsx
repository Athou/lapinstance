import { Button, Card, Elevation, H5 } from "@blueprintjs/core"
import React from "react"
import styled from "styled-components"
import { UserCharacter } from "../../api"
import { characterSpecLabels } from "../../api/utils"

const CardTitle = styled(H5)`
    display: flex;
`

const CardTitleText = styled.div`
    flex-grow: 1;
`

export const CharacterCard: React.FC<{ character: UserCharacter; onEdit: () => void }> = props => {
    return (
        <>
            <Card elevation={Elevation.TWO}>
                <CardTitle>
                    <CardTitleText>{props.character.name}</CardTitleText>
                    <Button icon="edit" onClick={props.onEdit} />
                </CardTitle>
                <div>{characterSpecLabels[props.character.spec]}</div>
                <div>{props.character.main ? "Main" : "Reroll"}</div>
            </Card>
        </>
    )
}
