import { Button, Checkbox, Classes, Dialog } from "@blueprintjs/core"
import _ from "lodash"
import React, { useState } from "react"
import { Col, Grid, Row } from "react-flexbox-grid"
import { User } from "../../api"
import { client } from "../../api/client"
import { ActionButton } from "../ActionButton"

type SelectableUser = User & { selected: boolean }

export const RaidNotificationButton: React.FC<{ raidId: number }> = props => {
    const [users, setUsers] = useState<SelectableUser[]>([])
    const [open, setOpen] = useState(false)

    const usersChunks = _.chunk(users, 10)

    const notificationButtonClicked = () => {
        client.raids.findMissingRaidSubscriptions(props.raidId).then(resp => {
            setUsers(resp.data.map(u => ({ ...u, selected: true })))
            setOpen(true)
        })
    }

    const checkboxClicked = (user: User) => {
        setUsers(users.map(u => (u !== user ? u : { ...u, selected: !u.selected })))
    }

    const sendNotifications = () => {
        client.raids
            .notifyMissingRaidSubscriptions(
                props.raidId,
                users.filter(u => u.selected).map(u => u.id)
            )
            .then(() => {
                setOpen(false)
            })
    }

    return (
        <>
            <ActionButton onClick={notificationButtonClicked} icon="notifications" marginRight />
            <Dialog isOpen={open}>
                <div className={Classes.DIALOG_HEADER}>Envoyer des rappels d'inscription</div>
                <div className={Classes.DIALOG_BODY}>
                    <Grid>
                        <Row>
                            {usersChunks.map(chunk => (
                                <Col md key={chunk[0].id}>
                                    {chunk.map(u => (
                                        <Checkbox key={u.id} label={u.name} checked={u.selected} onChange={() => checkboxClicked(u)} />
                                    ))}
                                </Col>
                            ))}
                        </Row>
                    </Grid>
                </div>
                <div className={Classes.DIALOG_FOOTER}>
                    <div className={Classes.DIALOG_FOOTER_ACTIONS}>
                        <Button onClick={() => setOpen(false)}>Fermer</Button>
                        <Button onClick={sendNotifications} intent="primary">
                            Envoyer
                        </Button>
                    </div>
                </div>
            </Dialog>
        </>
    )
}
