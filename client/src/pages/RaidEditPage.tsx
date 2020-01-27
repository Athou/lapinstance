import React, { useEffect, useState } from "react"
import { Raid } from "../api"
import { client } from "../api/client"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidEdit } from "../components/raids/RaidEdit"

export const RaidEditPage: React.FC<{ raidId: number }> = props => {
    const [raid, setRaid] = useState<Raid | undefined>()

    useEffect(() => {
        client.raids.getRaid(props.raidId).then(resp => setRaid(resp.data))
    }, [props.raidId])

    if (!raid) return <Loader />
    return (
        <>
            <PageTitle>Modification d'un raid</PageTitle>
            <RaidEdit raid={raid} />
        </>
    )
}
