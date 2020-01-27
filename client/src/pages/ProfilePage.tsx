import { Button, H4 } from "@blueprintjs/core"
import React, { useEffect, useState } from "react"
import { Col, Row } from "react-flexbox-grid"
import styled from "styled-components"
import { CharacterSpec, UserCharacter } from "../api"
import { client } from "../api/client"
import { useSession } from "../App"
import { CharacterCard } from "../components/characters/CharacterCard"
import { CharacterEdit } from "../components/characters/CharacterEdit"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"

type EditableUserCharacter = UserCharacter & { editMode: boolean }

const CardWrapper = styled.div`
    margin-bottom: 1rem;
`
export const ProfilePage: React.FC = () => {
    const [characters, setCharacters] = useState<EditableUserCharacter[]>([])
    const [loading, setLoading] = useState(false)
    const session = useSession()
    const userId = session.user.id!
    const showAddButton = characters.filter(c => !c.id).length === 0

    const addCharacter = () => {
        setCharacters(chars => [
            ...chars,
            {
                name: "",
                spec: CharacterSpec.DRUID_BALANCE,
                main: true,
                editMode: true,
                user: session.user
            }
        ])
    }

    const saveCharacter = (newCharacter: UserCharacter) => {
        client.users
            .saveUserCharacter(userId, newCharacter)
            .then(resp =>
                setCharacters(chars =>
                    chars.map(existing => (existing.id && existing.id !== resp.data.id ? existing : { ...resp.data, editMode: false }))
                )
            )
    }

    const editCharacter = (id: number) => {
        setCharacters(chars => chars.map(existing => (existing.id === id ? { ...existing, editMode: true } : existing)))
    }

    const cancelEditCharacter = (id?: number) => {
        if (id) {
            setCharacters(chars => chars.map(existing => (existing.id === id ? { ...existing, editMode: false } : existing)))
        } else {
            setCharacters(chars => chars.filter(c => c.id))
        }
    }

    useEffect(() => {
        setLoading(true)
        client.users
            .findAllUserCharacters(userId)
            .then(resp =>
                setCharacters(
                    resp.data.map(c => ({
                        ...c,
                        editMode: false
                    }))
                )
            )
            .finally(() => setLoading(false))
    }, [userId])

    if (loading) return <Loader />
    return (
        <>
            <PageTitle>Mon profil</PageTitle>

            <H4>Mes personnages</H4>
            <Row>
                <Col md={6}>
                    {characters.map(char => (
                        <CardWrapper key={char.id}>
                            {char.editMode ? (
                                <CharacterEdit character={char} onSave={saveCharacter} onCancel={() => cancelEditCharacter(char.id)} />
                            ) : (
                                <CharacterCard character={char} onEdit={() => editCharacter(char.id!)} />
                            )}
                        </CardWrapper>
                    ))}
                    {showAddButton && <Button intent="primary" text="Créer un personnage" onClick={addCharacter} />}
                </Col>
            </Row>
        </>
    )
}
