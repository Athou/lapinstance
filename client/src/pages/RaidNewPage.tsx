import React, { useEffect, useState } from "react"
import { RaidTextChannel } from "../api"
import { client } from "../api/client"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidEdit } from "../components/raids/RaidEdit"

export const RaidNewPage: React.FC = () => {
    const [raidTextChannels, setRaidTextChannels] = useState<RaidTextChannel[]>()

    useEffect(() => {
        client.raidTextChannels.getAll().then(resp => setRaidTextChannels(resp.data))
    }, [])

    if (!raidTextChannels) return <Loader />
    return (
        <>
            <PageTitle>Nouveau raid</PageTitle>
            <RaidEdit raidTextChannels={raidTextChannels} />
        </>
    )
}
