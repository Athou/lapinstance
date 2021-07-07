import { Toaster } from "@blueprintjs/core"
import React from "react"
import { RaidSubscription } from "../../api"
import { ActionButton } from "../ActionButton"

const toaster = Toaster.create()
export const RaidParticipantsCopyButton: React.FC<{ subscriptions: RaidSubscription[] }> = props => {
    const copyButtonClicked = () => {
        const text = props.subscriptions.map(sub => sub.character?.name).join(",")
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
