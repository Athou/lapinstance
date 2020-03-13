import { Button, H4, Radio, RadioGroup } from "@blueprintjs/core"
import React, { useState } from "react"
import styled from "styled-components"
import { RaidSubscription, RaidSubscriptionResponse, UserCharacter } from "../../api"
import { raidSubscriptionResponseLabels } from "../../api/utils"
import { ResponseIcon } from "../response-icons/ResponseIcon"
import { SpecIcon } from "../spec-icons/SpecIcon"

const IconWrapper = styled.span`
    margin-right: 0.5rem;
`

export const SubscriptionSelection: React.FC<{
    subscription?: RaidSubscription
    characters: UserCharacter[]
    onSave: (character: UserCharacter | undefined, response: RaidSubscriptionResponse | undefined) => void
}> = props => {
    const responses = [RaidSubscriptionResponse.LATE, RaidSubscriptionResponse.BENCH, RaidSubscriptionResponse.ABSENT]

    const [selectedCharacter, setSelectedCharacter] = useState(props.subscription?.character)
    const [selectedResponse, setSelectedResponse] = useState(props.subscription?.response)

    const handleChange = (e: React.FormEvent<HTMLInputElement>) => {
        const val = e.currentTarget.value

        const char = props.characters.find(c => String(c.id) === val)
        const resp = responses.find(r => r === val)
        if (char) {
            setSelectedCharacter(char)
            setSelectedResponse(undefined)
        } else {
            setSelectedCharacter(undefined)
            setSelectedResponse(resp)
        }
    }

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        props.onSave(selectedCharacter, selectedResponse)
    }

    return (
        <>
            <H4>Inscription</H4>
            <form onSubmit={handleSubmit}>
                <RadioGroup onChange={handleChange} selectedValue={selectedCharacter?.id ?? selectedResponse}>
                    {props.characters.map(c => (
                        <Radio key={c.id} value={c.id}>
                            <IconWrapper>
                                <SpecIcon spec={c.spec} />
                            </IconWrapper>
                            {c.name}
                        </Radio>
                    ))}

                    {responses.map(r => (
                        <Radio value={r} key={r}>
                            <IconWrapper>
                                <ResponseIcon response={r} />
                            </IconWrapper>
                            {raidSubscriptionResponseLabels[r]}
                        </Radio>
                    ))}
                </RadioGroup>
                <Button type="submit" intent="primary" text="Enregistrer" disabled={!selectedCharacter && !selectedResponse} />
            </form>
        </>
    )
}
