import { Alert, Card, Toaster } from "@blueprintjs/core"
import React, { useEffect, useState } from "react"
import Moment from "react-moment"
import { Link, useHistory } from "react-router-dom"
import { Box, Flex } from "reflexbox"
import styled from "styled-components"
import { Raid, RaidSubscription, RaidSubscriptionResponse, UserCharacter, UserRole } from "../api"
import { client } from "../api/client"
import { raidTypeLabels } from "../api/utils"
import { useSession } from "../App"
import { ActionButton } from "../components/ActionButton"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidNotificationButton } from "../components/raids/RaidNotificationButton"
import { RaidParticipantsCopyButton } from "../components/raids/RaidParticipantsCopyButton"
import { SubscriptionList } from "../components/subscriptions/SubscriptionList"
import { SubscriptionSelection } from "../components/subscriptions/SubscriptionSelection"
import { Routes } from "../Routes"

const toaster = Toaster.create()

const StyledCard = styled(Card)`
    margin-bottom: 1rem;
`

export const RaidPage: React.FC<{ raidId: number }> = props => {
    const [raid, setRaid] = useState<Raid>()
    const [subscriptions, setSubscriptions] = useState<RaidSubscription[]>([])
    const [userCharacters, setUserCharacters] = useState<UserCharacter[]>([])
    const [subscription, setSubscription] = useState<RaidSubscription>()
    const [deleteAlertOpen, setDeleteAlertOpen] = useState(false)
    const [loading, setLoading] = useState(false)

    const session = useSession()
    const history = useHistory()

    const participants = subscriptions.filter(sub => sub.response === "PRESENT")

    const editRaid = (raidId: number) => history.push(Routes.raid.edit.create({ raidId: String(raidId) }))
    const deleteRaid = (raidId: number) => client.raids.deleteRaid(raidId).then(() => history.push(Routes.raid.list.create({})))

    const saveSubscription = (character: UserCharacter | undefined, response: RaidSubscriptionResponse | undefined) => {
        raid &&
            client.raids
                .saveRaidSubscription(props.raidId, {
                    response: response ?? RaidSubscriptionResponse.PRESENT,
                    userId: session.user.id,
                    characterId: character?.id
                })
                .then(resp => {
                    setSubscriptions(subs => {
                        const subsWithRemovedUserSubs = subs.filter(s => s.user.id !== session.user.id)
                        return [...subsWithRemovedUserSubs, resp.data]
                    })
                    toaster.show({
                        message: "Réponse enregistrée",
                        icon: "tick",
                        intent: "success"
                    })
                })
    }

    useEffect(() => {
        setLoading(true)
        Promise.all([
            client.raids.getRaid(props.raidId),
            client.raids.findRaidSubscriptions(props.raidId),
            client.users.findAllUserCharacters(session.user.id)
        ])
            .then(([raidResp, subscriptionsResp, userCharactersResp]) => {
                setRaid(raidResp.data)
                setSubscriptions(subscriptionsResp.data)
                setUserCharacters(userCharactersResp.data)

                const userSubscription = subscriptionsResp.data.find(s => s.user.id === session.user.id)
                setSubscription(userSubscription)
            })
            .finally(() => setLoading(false))
    }, [props.raidId, session.user.id])

    if (loading || !raid) return <Loader />
    return (
        <>
            <Flex>
                <Box flexGrow={1}>
                    <PageTitle>
                        Raid {raidTypeLabels[raid.raidType]} - <Moment format="dddd DD MMMM YYYY HH:mm">{raid.date}</Moment>
                    </PageTitle>
                </Box>
                <Box>
                    {session.hasRole(UserRole.ADMIN) && (
                        <>
                            <RaidParticipantsCopyButton subscriptions={participants} />
                            <RaidNotificationButton raidId={raid.id} />
                            <ActionButton onClick={() => editRaid(raid.id)} icon="edit" marginRight />
                            <ActionButton onClick={() => setDeleteAlertOpen(true)} icon="trash" intent="danger" />
                            <Alert
                                confirmButtonText="Effacer"
                                cancelButtonText="Annuler"
                                icon="trash"
                                intent="danger"
                                isOpen={deleteAlertOpen}
                                onCancel={() => setDeleteAlertOpen(false)}
                                onConfirm={() => deleteRaid(raid.id)}
                            >
                                <p>Effacer le raid ?</p>
                            </Alert>
                        </>
                    )}
                </Box>
            </Flex>

            <StyledCard>
                <p>{raid.comment}</p>

                <SubscriptionList subscriptions={subscriptions} />
            </StyledCard>

            <StyledCard>
                {userCharacters.length === 0 ? (
                    <div>
                        <span>Pour participer à ce raid, crée un personnage sur ta </span>
                        <Link to={Routes.profile.create({})}>page de profil</Link>
                        <span>.</span>
                    </div>
                ) : (
                    <SubscriptionSelection subscription={subscription} characters={userCharacters} onSave={saveSubscription} />
                )}
            </StyledCard>
        </>
    )
}
