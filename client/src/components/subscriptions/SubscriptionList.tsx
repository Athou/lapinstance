import { H4, H5 } from "@blueprintjs/core"
import React from "react"
import { Col, Grid, Row } from "react-flexbox-grid"
import { useHistory } from "react-router-dom"
import styled from "styled-components"
import { RaidSubscription } from "../../api"
import { CharacterRole, characterRoleLabels, specToRoleMapping } from "../../api/utils"
import { useSession } from "../../App"
import { Routes } from "../../Routes"
import { SpecIcon } from "../spec-icons/SpecIcon"

export const SubscriptionList: React.FC<{ subscriptions: RaidSubscription[] }> = props => {
    const participants = props.subscriptions.filter(sub => sub.response !== "ABSENT")
    const absents = props.subscriptions.filter(sub => sub.response === "ABSENT")
    const lates = props.subscriptions.filter(sub => sub.response === "LATE")
    const benches = props.subscriptions.filter(sub => sub.response === "BENCH")

    const roleList = (role: CharacterRole) => {
        const subs = participants.filter(sub => sub.character && specToRoleMapping[sub.character.spec] === role)
        const title = characterRoleLabels[role]
        return <RaidSubscriptionList title={title} subscriptions={subs} />
    }

    return (
        <>
            <H4>Inscrits ({participants.length})</H4>
            <Grid>
                <Row>
                    <Col xs>{roleList(CharacterRole.TANK)}</Col>
                    <Col xs>{roleList(CharacterRole.MELEE)}</Col>
                    <Col xs>{roleList(CharacterRole.RANGED)}</Col>
                    <Col xs>{roleList(CharacterRole.HEAL)}</Col>
                </Row>
                <Row>
                    <Col xs>
                        <RaidSubscriptionList title="En retard" subscriptions={lates} />
                    </Col>
                    <Col xs>
                        <RaidSubscriptionList title="Bench" subscriptions={benches} />
                    </Col>
                    <Col xs>
                        <RaidSubscriptionList title="Absents" subscriptions={absents} />
                    </Col>
                    <Col xs></Col>
                </Row>
            </Grid>
        </>
    )
}

type StyledCharacterNameProps = {
    user: boolean
}
const StyledCharacterName = styled.span`
    margin-left: 0.2rem;
    color: ${(props: StyledCharacterNameProps) => (props.user ? "yellow" : "inherit")};
    &:hover {
        text-decoration: underline;
        cursor: pointer;
    }
`

const RaidSubscriptionList: React.FC<{ title: string; subscriptions: RaidSubscription[] }> = props => {
    const session = useSession()
    const history = useHistory()

    const buildSubscriptionDisplayName = (subscription: RaidSubscription) =>
        subscription.character ? subscription.character.name : subscription.user.name

    const sortedSubscriptions = props.subscriptions.sort((a, b) => {
        const nameA = buildSubscriptionDisplayName(a)
        const nameB = buildSubscriptionDisplayName(b)

        if (a.character && b.character) {
            if (a.character.spec === b.character.spec) return nameA.localeCompare(nameB)
            return a.character.spec.localeCompare(b.character.spec)
        } else {
            return nameA.localeCompare(nameB)
        }
    })
    return (
        <>
            <H5>{`${props.title} (${sortedSubscriptions.length})`}</H5>
            {sortedSubscriptions.map(sub => (
                <div key={sub.id}>
                    {sub.character && <SpecIcon spec={sub.character.spec} />}
                    <StyledCharacterName
                        user={session.user.id === sub.user.id}
                        onClick={() => history.push(Routes.user.show.create({ userId: String(sub.user.id!) }))}
                    >
                        {buildSubscriptionDisplayName(sub)}
                    </StyledCharacterName>
                </div>
            ))}
        </>
    )
}
