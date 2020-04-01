import { Button, H4 } from "@blueprintjs/core"
import moment from "moment"
import React, { useEffect, useState } from "react"
import { Col, Row } from "react-flexbox-grid"
import { useHistory } from "react-router-dom"
import styled from "styled-components"
import { Raid, RaidSubscription, RaidType, UserRole } from "../api"
import { client } from "../api/client"
import { useSession } from "../App"
import { Loader } from "../components/Loader"
import { PageTitle } from "../components/PageTitle"
import { RaidCalendar, RaidReset } from "../components/raids/RaidCalendar"
import { RaidCard } from "../components/raids/RaidCard"
import { Routes } from "../Routes"

const RaidSection = styled.div`
    margin-bottom: 2rem;
`

export const RaidsPage: React.FC = () => {
    const [raids, setRaids] = useState<Raid[]>([])
    const [userSubscriptions, setUserSubscriptions] = useState<RaidSubscription[]>()
    const [onyxiaResets, setOnyxiaResets] = useState<number[]>([])
    const [loading, setLoading] = useState(false)
    const history = useHistory()
    const session = useSession()
    const showRaid = (raidId: number) => history.push(Routes.raid.show.create({ raidId: String(raidId) }))

    useEffect(() => {
        setLoading(true)

        const onyxiaFrom = moment()
            .subtract(1, "month")
            .startOf("month")
            .valueOf()
        const onyxiaUntil = moment()
            .add(1, "month")
            .endOf("month")
            .valueOf()

        Promise.all([
            client.raids.findAllRaids(),
            client.users.findAllSubscriptions(session.user.id),
            client.raidTypes.nextReset(RaidType.ONYXIA, {
                from: onyxiaFrom,
                until: onyxiaUntil
            })
        ])
            .then(([raidsResp, subsResp, onyxiaResp]) => {
                setRaids(raidsResp.data)
                setUserSubscriptions(subsResp.data)
                setOnyxiaResets(onyxiaResp.data)
            })
            .finally(() => setLoading(false))
    }, [session.user.id])

    const now = new Date().getTime()
    const futureRaids = raids.filter(raid => now < raid.date).sort((a, b) => a.date - b.date)
    const resets: RaidReset[] = onyxiaResets.map(r => ({
        date: r,
        raidType: RaidType.ONYXIA
    }))

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
                    <RaidCalendar raids={raids} resets={resets} onRaidSelect={raidId => showRaid(raidId)} />
                </Col>
            </Row>
        </>
    )
}
