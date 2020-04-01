import { Alert, Button, H4, Switch, Toaster } from "@blueprintjs/core"
import React, { useEffect, useState } from "react"
import { Col, Row } from "react-flexbox-grid"
import styled from "styled-components"
import { CharacterSpec, User, UserCharacter, UserRole } from "../api"
import { client } from "../api/client"
import { useSession } from "../App"
import { CharacterCard } from "../components/characters/CharacterCard"
import { CharacterEdit } from "../components/characters/CharacterEdit"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"

type EditableUserCharacter = UserCharacter & { editMode: boolean }
const toaster = Toaster.create()
const NEW_CHARACTER_ID = 0

const CardWrapper = styled.div`
    margin-bottom: 1rem;
`

export const UserPage: React.FC<{ userId: number }> = props => {
    const [user, setUser] = useState<User>()
    const [characters, setCharacters] = useState<EditableUserCharacter[]>([])
    const [disablingConfirmationOpen, setDisablingConfirmationOpen] = useState(false)
    const [loading, setLoading] = useState(false)
    const session = useSession()

    const editable = session.hasRole(UserRole.ADMIN) || session.user.id === props.userId

    const showEditButton = editable
    const showAddButton = editable && characters.filter(c => c.id === 0).length === 0

    const enableUser = () => user && client.users.saveUser(user.id, { disabled: false }).then(resp => setUser(resp.data))
    const disableUser = () => {
        user &&
            client.users.saveUser(user.id, { disabled: true }).then(resp => {
                setUser(resp.data)
                setDisablingConfirmationOpen(false)
            })
    }

    const addCharacter = () => {
        user &&
            setCharacters(chars => [
                ...chars,
                {
                    id: NEW_CHARACTER_ID,
                    name: "",
                    spec: CharacterSpec.DRUID_BALANCE,
                    main: true,
                    editMode: true,
                    user: user
                }
            ])
    }

    const saveCharacter = (char: UserCharacter) => {
        client.users
            .saveUserCharacter(props.userId, {
                characterId: char.id === NEW_CHARACTER_ID ? undefined : char.id,
                name: char.name,
                spec: char.spec,
                main: char.main
            })
            .then(resp => {
                setCharacters(chars => {
                    if (char.id === NEW_CHARACTER_ID) {
                        return chars.map(existing => (existing.id !== NEW_CHARACTER_ID ? existing : { ...resp.data, editMode: false }))
                    } else {
                        return chars.map(existing => (existing.id !== resp.data.id ? existing : { ...resp.data, editMode: false }))
                    }
                })
                toaster.show({
                    message: "Personnage enregistré",
                    intent: "success",
                    icon: "tick"
                })
            })
    }

    const editCharacter = (id: number) => {
        setCharacters(chars => chars.map(existing => (existing.id === id ? { ...existing, editMode: true } : existing)))
    }

    const cancelEditCharacter = (id: number) => {
        if (id === NEW_CHARACTER_ID) {
            setCharacters(chars => chars.filter(c => c.id !== NEW_CHARACTER_ID))
        } else {
            setCharacters(chars => chars.map(existing => (existing.id === id ? { ...existing, editMode: false } : existing)))
        }
    }

    useEffect(() => {
        setLoading(true)
        Promise.all([client.users.getUser(props.userId), client.users.findAllUserCharacters(props.userId)])
            .then(([userResp, charsResp]) => {
                setUser(userResp.data)
                setCharacters(
                    charsResp.data.map(c => ({
                        ...c,
                        editMode: false
                    }))
                )
            })
            .finally(() => setLoading(false))
    }, [props.userId])

    if (loading || !user) return <Loader />
    return (
        <>
            <PageTitle>{user.name}</PageTitle>

            {session.hasRole(UserRole.ADMIN) && (
                <Switch
                    checked={!user.disabled}
                    label="Actif"
                    onChange={() => (user.disabled ? enableUser() : setDisablingConfirmationOpen(true))}
                />
            )}
            <Alert
                cancelButtonText="Annuler"
                confirmButtonText="Désactiver"
                intent="danger"
                isOpen={disablingConfirmationOpen}
                onCancel={() => setDisablingConfirmationOpen(false)}
                onConfirm={() => disableUser()}
            >
                Désactiver le compte de {user.name} ?
            </Alert>

            <H4>Personnages</H4>
            <Row>
                <Col md={6}>
                    {characters.map(char => (
                        <CardWrapper key={char.id}>
                            {char.editMode ? (
                                <CharacterEdit character={char} onSave={saveCharacter} onCancel={() => cancelEditCharacter(char.id)} />
                            ) : (
                                <CharacterCard character={char} editable={showEditButton} onEdit={() => editCharacter(char.id)} />
                            )}
                        </CardWrapper>
                    ))}
                    {showAddButton && <Button intent="primary" text="Créer un personnage" onClick={addCharacter} />}
                </Col>
            </Row>
        </>
    )
}
