import { Toaster } from "@blueprintjs/core"
import _ from "lodash"
import React from "react"
import { RaidSubscription } from "../../api"
import { CharacterRole, specToRoleMapping } from "../../api/utils"
import { ActionButton } from "../ActionButton"

const toaster = Toaster.create()
export const RaidParticipantsCopyButton: React.FC<{ subscriptions: RaidSubscription[] }> = props => {
    const copyButtonClicked = () => {
        const sortedSubscriptions = _.sortBy(
            props.subscriptions,
            s => {
                const spec = s.character?.spec
                if (!spec) return 99

                const role = specToRoleMapping[spec]
                if (role === CharacterRole.TANK) return 0
                if (role === CharacterRole.HEAL) return 1
                return 2
            },
            s => s.character?.spec
        )
        const text = sortedSubscriptions
            .map(sub => {
                const lines = [sub.character?.name]
                const spec = sub.character?.spec
                if (spec) {
                    const role = specToRoleMapping[spec]
                    if (role === CharacterRole.TANK || role === CharacterRole.HEAL) lines.push(role)
                    else lines.push("DPS")
                }
                return lines.join("\t")
            })
            .join("\n")
        navigator.clipboard.writeText(text).then(
            () =>
                toaster.show({
                    message: "Liste copiée",
                    intent: "success",
                    icon: "tick",
                }),
            () =>
                toaster.show({
                    message: "Erreur",
                    intent: "danger",
                    icon: "cross",
                })
        )
    }

    return (
        <>
            <ActionButton onClick={copyButtonClicked} icon="clipboard" marginRight />
        </>
    )
}
