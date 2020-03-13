import { Card, Checkbox, FormGroup, HTMLSelect, InputGroup } from "@blueprintjs/core"
import React, { useState } from "react"
import { CharacterSpec, UserCharacter } from "../../api"
import { characterSpecLabels, CharacterSpecs } from "../../api/utils"
import { useSession } from "../../App"
import { ActionButton } from "../ActionButton"

export const CharacterEdit: React.FC<{
    character: UserCharacter
    onSave: (character: UserCharacter) => void
    onCancel: () => void
}> = props => {
    const [name, setName] = useState(props.character.name)
    const [spec, setSpec] = useState(props.character.spec)
    const [main, setMain] = useState(props.character.main)

    const session = useSession()

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        props.onSave({
            id: props.character.id,
            name,
            spec,
            main,
            user: session.user
        })
    }

    return (
        <>
            <Card elevation={2}>
                <form onSubmit={handleSubmit}>
                    <FormGroup label="Nom">
                        <InputGroup required value={name} onChange={(e: React.ChangeEvent<HTMLInputElement>) => setName(e.target.value)} />
                    </FormGroup>

                    <FormGroup label="Spec">
                        <HTMLSelect value={spec} onChange={e => setSpec(e.target.value as CharacterSpec)}>
                            {CharacterSpecs.map(s => (
                                <option key={s} value={s}>
                                    {characterSpecLabels[s]}
                                </option>
                            ))}
                        </HTMLSelect>
                    </FormGroup>

                    <Checkbox label="Main" checked={main} onChange={e => setMain(!main)} />

                    <ActionButton intent="primary" type="submit" text="Sauver" marginRight />
                    <ActionButton text="Annuler" onClick={props.onCancel} />
                </form>
            </Card>
        </>
    )
}
