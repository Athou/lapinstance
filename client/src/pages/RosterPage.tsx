import React, { useEffect, useState } from "react"
import { RaidType, RosterMember, UserCharacter } from "../api"
import { client } from "../api/client"
import { Loader } from "../components/Loader"
import { RosterTable } from "../components/roster/RosterTable"

export const RosterPage: React.FC = () => {
    const [characters, setCharacters] = useState<UserCharacter[]>([])
    const [roster, setRoster] = useState<RosterMember[]>([])
    const [loading, setLoading] = useState(false)

    const addMember = (userCharacter: UserCharacter, raidType: RaidType) => {
        client.rosters
            .addRosterMember({
                raidType,
                userCharacter
            })
            .then(resp => setRoster(roster => [...roster, resp.data]))
    }

    const removeMember = (userCharacter: UserCharacter, raidType: RaidType) => {
        client.rosters
            .removeRosterMember({
                raidType,
                userCharacter
            })
            .then(() => setRoster(roster => roster.filter(r => r.raidType !== raidType || r.userCharacter.id !== userCharacter.id)))
    }

    useEffect(() => {
        setLoading(true)
        Promise.all([client.userCharacters.findAllUserCharacters(), client.rosters.findAllRosterMembers()])
            .then(([charactersResp, rosterResp]) => {
                setCharacters(charactersResp.data)
                setRoster(rosterResp.data)
            })
            .finally(() => setLoading(false))
    }, [])

    if (loading) return <Loader />
    return (
        <>
            <RosterTable characters={characters} roster={roster} onSelect={addMember} onDeselect={removeMember} />
        </>
    )
}
