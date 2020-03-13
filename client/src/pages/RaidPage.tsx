import { Alert, Card, Intent, Toaster } from "@blueprintjs/core"
import React, { useEffect, useState } from "react"
import Moment from "react-moment"
import { Link, useHistory } from "react-router-dom"
import styled from "styled-components"
import { Raid, RaidSubscription, RaidSubscriptionResponse, RosterMember, UserCharacter, UserRole } from "../api"
import { client } from "../api/client"
import { raidTypeLabels } from "../api/utils"
import { useApplicationSettings, useSession } from "../App"
import { ActionButton } from "../components/ActionButton"
import { Box, Flex } from "../components/flexbox"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidNotificationButton } from "../components/raids/RaidNotificationButton"
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
    const [rosterMemberships, setRosterMemberships] = useState<RosterMember[]>([])
    const [subscription, setSubscription] = useState<RaidSubscription>()
    const [deleteAlertOpen, setDeleteAlertOpen] = useState(false)
    const [loading, setLoading] = useState(false)

    const session = useSession()
    const applicationSettings = useApplicationSettings()
    const history = useHistory()

    const editRaid = (raidId: number) => history.push(Routes.raid.edit.create({ raidId: String(raidId) }))
    const deleteRaid = (raidId: number) => client.raids.deleteRaid(raidId).then(() => history.push(Routes.raid.list.create({})))

    const isCharacterSelectable = (char: UserCharacter) => {
        if (!applicationSettings.roasterEnabled) return true
        return rosterMemberships.some(rm => rm.raidType === raid?.raidType && rm.userCharacter.id === char.id)
    }

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
                        intent: Intent.SUCCESS
                    })
                })
    }

    useEffect(() => {
        setLoading(true)
        Promise.all([
            client.raids.getRaid(props.raidId),
            client.raids.findRaidSubscriptions(props.raidId),
            client.users.findAllUserCharacters(session.user.id!),
            client.users.findAllRosterMemberships(session.user.id!)
        ])
            .then(([raidResp, subscriptionsResp, userCharactersResp, membershipsResp]) => {
                setRaid(raidResp.data)
                setSubscriptions(subscriptionsResp.data)
                setUserCharacters(userCharactersResp.data)
                setRosterMemberships(membershipsResp.data)

                const userSubscription = subscriptionsResp.data.find(s => s.user.id === session.user.id)
                setSubscription(userSubscription)
            })
            .finally(() => setLoading(false))
    }, [props.raidId, session.user.id])

    if (loading || !raid) return <Loader />
    return (
        <>
            <Flex>
                <Box grow={1}>
                    <PageTitle>
                        Raid {raidTypeLabels[raid.raidType]} - <Moment format="dddd DD MMMM YYYY HH:mm">{raid.date}</Moment>
                    </PageTitle>
                </Box>
                <Box>
                    {session.hasRole(UserRole.ADMIN) && (
                        <>
                            <RaidNotificationButton raidId={raid.id!} />
                            <ActionButton onClick={() => editRaid(raid.id!)} icon="edit" marginRight />
                            <ActionButton onClick={() => setDeleteAlertOpen(true)} icon="trash" intent="danger" />
                            <Alert
                                confirmButtonText="Effacer"
                                cancelButtonText="Annuler"
                                icon="trash"
                                intent={Intent.DANGER}
                                isOpen={deleteAlertOpen}
                                onCancel={() => setDeleteAlertOpen(false)}
                                onConfirm={() => deleteRaid(raid.id!)}
                            >
                                <p>Effacer le raid ?</p>
                            </Alert>
                        </>
                    )}
                </Box>
            </Flex>

            <StyledCard>
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
                    <SubscriptionSelection
                        subscription={subscription}
                        characters={userCharacters}
                        isCharacterSelectable={isCharacterSelectable}
                        onSave={saveSubscription}
                    />
                )}
            </StyledCard>
        </>
    )
}
