import React from "react"
import { RaidType } from "../api"
import { PageTitle } from "../components/PageTitle"
import { RaidEdit } from "../components/raids/RaidEdit"

export const RaidNewPage: React.FC = () => {
    const date = new Date()
    date.setHours(20, 30, 0, 0)

    return (
        <>
            <PageTitle>Nouveau raid</PageTitle>
            <RaidEdit
                raid={{
                    raidType: RaidType.ONYXIA,
                    date: date.getTime()
                }}
            />
        </>
    )
}
