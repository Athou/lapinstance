import React, { useEffect, useState } from "react"
import { Raid, RaidTextChannel } from "../api"
import { client } from "../api/client"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidEdit } from "../components/raids/RaidEdit"

export const RaidEditPage: React.FC<{ raidId: number }> = props => {
    const [raid, setRaid] = useState<Raid | undefined>()
    const [raidTextChannels, setRaidTextChannels] = useState<RaidTextChannel[]>()

    useEffect(() => {
        client.raids.getRaid(props.raidId).then(resp => setRaid(resp.data))
        client.raidTextChannels.getAll().then(resp => setRaidTextChannels(resp.data))
    }, [props.raidId])

    if (!raid || !raidTextChannels) return <Loader />
    return (
        <>
            <PageTitle>Modification d'un raid</PageTitle>
            <RaidEdit raid={raid} raidTextChannels={raidTextChannels} />
        </>
    )
}
