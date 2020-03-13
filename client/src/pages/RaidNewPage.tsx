import React from "react"
import { PageTitle } from "../components/PageTitle"
import { RaidEdit } from "../components/raids/RaidEdit"

export const RaidNewPage: React.FC = () => {
    return (
        <>
            <PageTitle>Nouveau raid</PageTitle>
            <RaidEdit />
        </>
    )
}
