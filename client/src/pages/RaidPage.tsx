import { Alert, Card, FormGroup, Switch, Toaster } from "@blueprintjs/core"
import _ from "lodash"
import React, { useEffect, useState } from "react"
import Moment from "react-moment"
import { Link, useHistory } from "react-router-dom"
import { Box, Flex } from "reflexbox"
import styled from "styled-components"
import { Raid, RaidSubscription, RaidSubscriptionResponse, UserCharacter, UserRole } from "../api"
import { client } from "../api/client"
import { raidTypeLabels } from "../api/utils"
import { ActionButton } from "../components/ActionButton"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidNotificationButton } from "../components/raids/RaidNotificationButton"
import { RaidParticipantsCopyButton } from "../components/raids/RaidParticipantsCopyButton"
import { SubscriptionList, SubscriptionModel } from "../components/subscriptions/SubscriptionList"
import { SubscriptionSelection } from "../components/subscriptions/SubscriptionSelection"
import { UserPicker } from "../components/UserPicker"
import { useSession } from "../hooks/useSession"
import { Routes } from "../Routes"

const toaster = Toaster.create()

const StyledCard = styled(Card)`
    margin-bottom: 1rem;
`

export const RaidPage: React.FC<{ raidId: number }> = props => {
    const session = useSession()
    const history = useHistory()

    const [raid, setRaid] = useState<Raid>()
    const [subscriptions, setSubscriptions] = useState<RaidSubscription[]>([])
    const [userCharacters, setUserCharacters] = useState<UserCharacter[]>([])
    const [subscription, setSubscription] = useState<RaidSubscription>()
    const [showIndexes, setShowIndexes] = useState(false)
    const [deleteAlertOpen, setDeleteAlertOpen] = useState(false)
    const [loading, setLoading] = useState(false)
    const [impersonatedUserId, setImpersonatedUserId] = useState(session.user.id)

    const participants = subscriptions.filter(sub => sub.response === "PRESENT")
    const impersonatedUserCharacters = userCharacters.filter(c => c.user.id === impersonatedUserId)
    const subscriptionModels: SubscriptionModel[] = _.sortBy(subscriptions, s => s.date).map((s, i) => ({
        ...s,
        mainCharacterName: userCharacters.find(c => c.main && c.user.id === s.user.id)?.name,
        index: i,
    }))

    const editRaid = (raidId: number) => history.push(Routes.raid.edit.create({ raidId: String(raidId) }))
    const deleteRaid = (raidId: number) => client.raids.deleteRaid(raidId).then(() => history.push(Routes.raid.list.create({})))
    const saveSubscription = (character: UserCharacter | undefined, response: RaidSubscriptionResponse | undefined) => {
        if (raid) {
            client.raids
                .saveRaidSubscription(props.raidId, {
                    response: response ?? RaidSubscriptionResponse.PRESENT,
                    userId: impersonatedUserId,
                    characterId: character?.id,
                })
                .then(resp => {
                    setSubscriptions(subs => {
                        const subsWithRemovedUserSubs = subs.filter(s => s.user.id !== impersonatedUserId)
                        return [...subsWithRemovedUserSubs, resp.data]
                    })
                    toaster.show({
                        message: "Réponse enregistrée",
                        icon: "tick",
                        intent: "success",
                    })
                })
        }
    }

    useEffect(() => {
        setLoading(true)
        Promise.all([
            client.raids.getRaid(props.raidId),
            client.raids.findRaidSubscriptions(props.raidId),
            client.userCharacters.findAllUserCharacters(),
        ])
            .then(([raidResp, subscriptionsResp, userCharactersResp]) => {
                setRaid(raidResp.data)
                setSubscriptions(subscriptionsResp.data)
                setUserCharacters(userCharactersResp.data)

                const userSubscription = subscriptionsResp.data.find(s => s.user.id === impersonatedUserId)
                setSubscription(userSubscription)
            })
            .finally(() => setLoading(false))

        const wsClient = client.newWsClient()
        wsClient.onConnect = () => {
            wsClient.subscribe(`/topic/raid/${props.raidId}/subscriptions`, msg => {
                const sub = JSON.parse(msg.body) as RaidSubscription
                client.raids.findRaidSubscriptions(props.raidId).then(resp => {
                    setSubscriptions(resp.data)

                    if (sub.user.id !== impersonatedUserId) {
                        toaster.show({
                            message: `${sub.user.name} s'est enregistré`,
                            icon: "arrow-right",
                            intent: "primary",
                        })
                    }
                })
            })
        }
        wsClient.activate()

        return () => wsClient.deactivate()
    }, [props.raidId, impersonatedUserId])

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
                    <RaidParticipantsCopyButton subscriptions={participants} />
                    {session.hasRole(UserRole.ADMIN) && (
                        <>
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

                <SubscriptionList subscriptions={subscriptionModels} showIndexes={showIndexes} />
                <Switch checked={showIndexes} label="Ordre d'inscription" onChange={() => setShowIndexes(!showIndexes)} />
            </StyledCard>

            <StyledCard>
                {impersonatedUserCharacters.length === 0 ? (
                    <div>
                        <span>Pour participer à ce raid, crée un personnage sur ta </span>
                        <Link to={Routes.profile.create({})}>page de profil</Link>
                        <span>.</span>
                    </div>
                ) : (
                    <>
                        {session.hasRole(UserRole.ADMIN) && (
                            <FormGroup label="Se faire passer pour">
                                <UserPicker userId={impersonatedUserId} onChange={id => setImpersonatedUserId(id)} />
                            </FormGroup>
                        )}
                        <SubscriptionSelection
                            subscription={subscription}
                            characters={impersonatedUserCharacters}
                            onSave={saveSubscription}
                        />
                    </>
                )}
            </StyledCard>
        </>
    )
}
