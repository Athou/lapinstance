import { Button, H4, Switch } from "@blueprintjs/core"
import moment from "moment"
import React, { useEffect, useState } from "react"
import { Col, Row } from "react-flexbox-grid"
import { useHistory } from "react-router-dom"
import styled from "styled-components"
import { Raid, RaidResetDuration, RaidSubscription, UserRole } from "../api"
import { client } from "../api/client"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidCalendar, RaidReset } from "../components/raids/RaidCalendar"
import { RaidCard } from "../components/raids/RaidCard"
import { useSession } from "../hooks/useSession"
import { Routes } from "../Routes"

const RaidSection = styled.div`
    margin-bottom: 2rem;
`

export const RaidsPage: React.FC = () => {
    const [raids, setRaids] = useState<Raid[]>([])
    const [raidResets, setRaidResets] = useState<RaidReset[]>([])
    const [raidResetFilter, setRaidResetFilter] = useState<Map<RaidResetDuration, boolean>>(new Map<RaidResetDuration, boolean>())
    const [userSubscriptions, setUserSubscriptions] = useState<RaidSubscription[]>()
    const [loading, setLoading] = useState(false)
    const history = useHistory()
    const session = useSession()
    const showRaid = (raidId: number) => history.push(Routes.raid.show.create({ raidId: String(raidId) }))

    useEffect(() => {
        setLoading(true)

        const raidResetFrom = moment().subtract(1, "month").startOf("month").valueOf()
        const raidResetUntil = moment().add(1, "month").endOf("month").valueOf()

        Promise.all([
            client.raids.findAllRaids(),
            client.users.findAllSubscriptions(session.user.id),
            client.raidTypes.nextReset(RaidResetDuration.FIVE_DAYS, {
                from: raidResetFrom,
                until: raidResetUntil,
            }),
            client.raidTypes.nextReset(RaidResetDuration.THREE_DAYS, {
                from: raidResetFrom,
                until: raidResetUntil,
            }),
        ])
            .then(([raidsResp, subsResp, fiveDaysResetResp, threeDaysResetResp]) => {
                setRaids(raidsResp.data)
                setUserSubscriptions(subsResp.data)

                const resets: RaidReset[] = []
                resets.push(
                    ...fiveDaysResetResp.data.map(r => ({
                        date: r,
                        label: "5J",
                        raidResetDuration: RaidResetDuration.FIVE_DAYS,
                    }))
                )
                resets.push(
                    ...threeDaysResetResp.data.map(r => ({
                        date: r,
                        label: "3J",
                        raidResetDuration: RaidResetDuration.THREE_DAYS,
                    }))
                )
                setRaidResets(resets)
            })
            .finally(() => setLoading(false))
    }, [session.user.id])

    const now = new Date().getTime()
    const futureRaids = raids.filter(raid => now < raid.date).sort((a, b) => a.date - b.date)
    const filteredRaidResets = raidResets.filter(reset => raidResetFilter.get(reset.raidResetDuration))

    if (loading) return <Loader />
    return (
        <>
            <PageTitle>Raids</PageTitle>
            <Row>
                <Col md={3}>
                    <RaidSection>
                        <H4>Raids à venir</H4>
                        {futureRaids.map(raid => (
                            <RaidCard
                                key={raid.id}
                                raid={raid}
                                userSubscription={userSubscriptions?.find(s => s.raid.id === raid.id)}
                                onClick={raidId => showRaid(raidId)}
                            />
                        ))}
                        {session.hasRole(UserRole.ADMIN) && (
                            <Button onClick={() => history.push(Routes.raid.new.create({}))}>Nouveau raid</Button>
                        )}
                    </RaidSection>
                </Col>
                <Col md={9}>
                    <H4>Calendrier</H4>
                    <RaidCalendar raids={raids} resets={filteredRaidResets} onRaidSelect={raidId => showRaid(raidId)} />
                    <Switch
                        inline
                        checked={raidResetFilter.get(RaidResetDuration.FIVE_DAYS)}
                        onClick={() => {
                            raidResetFilter.set(RaidResetDuration.FIVE_DAYS, !raidResetFilter.get(RaidResetDuration.FIVE_DAYS))
                            setRaidResetFilter(new Map(raidResetFilter))
                        }}
                        label="Reset 5J"
                    />
                    <Switch
                        inline
                        checked={raidResetFilter.get(RaidResetDuration.THREE_DAYS)}
                        onClick={() => {
                            raidResetFilter.set(RaidResetDuration.THREE_DAYS, !raidResetFilter.get(RaidResetDuration.THREE_DAYS))
                            setRaidResetFilter(new Map(raidResetFilter))
                        }}
                        label="Reset 3J"
                    />
                </Col>
            </Row>
        </>
    )
}
