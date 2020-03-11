import { HTMLTable, Icon } from "@blueprintjs/core"
import React, { useEffect, useState } from "react"
import { useHistory } from "react-router-dom"
import styled from "styled-components"
import { UserCharacter } from "../api"
import { client } from "../api/client"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { SpecIcon } from "../components/spec-icons/SpecIcon"
import { Routes } from "../Routes"

const StyledTable = styled(HTMLTable)`
    width: 100%;
`

const StyledCharacterName = styled.span`
    margin-left: 0.2rem;
    margin-right: 0.5rem;
`

export const UsersPage: React.FC = () => {
    const [userCharacters, setUserCharacters] = useState<UserCharacter[]>([])
    const [loading, setLoading] = useState(false)

    const history = useHistory()

    useEffect(() => {
        setLoading(true)

        client.userCharacters
            .findAllUserCharacters()
            .then(resp =>
                setUserCharacters(
                    resp.data.sort((a, b) => {
                        if (a.spec === b.spec) return a.name.localeCompare(b.name)
                        return a.spec.localeCompare(b.spec)
                    })
                )
            )
            .finally(() => setLoading(false))
    }, [])

    const row = (char: UserCharacter) => {
        let rerolls = userCharacters.filter(c => !c.main && c.user.id === char.user.id).sort((a, b) => a.name.localeCompare(b.name))

        return (
            <tr onClick={() => history.push(Routes.user.show.create({ userId: String(char.user.id!) }))}>
                <td>
                    <SpecIcon spec={char.spec} />
                    <StyledCharacterName>{char.name}</StyledCharacterName>
                </td>
                <td>
                    {rerolls.map(r => (
                        <>
                            <SpecIcon spec={r.spec} />
                            <StyledCharacterName>{r.name}</StyledCharacterName>
                        </>
                    ))}
                </td>
                <td>
                    <Icon icon={char.user.disabled ? "cross" : "tick"} />
                </td>
            </tr>
        )
    }

    if (loading) return <Loader />
    return (
        <>
            <PageTitle>Joueurs</PageTitle>
            <StyledTable interactive>
                <thead>
                    <tr>
                        <th>Joueur</th>
                        <th>Rerolls</th>
                        <th>Actif</th>
                    </tr>
                </thead>
                <tbody>{userCharacters.filter(c => c.main).map(c => row(c))}</tbody>
            </StyledTable>
        </>
    )
}
