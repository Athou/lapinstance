import { H4, H5, Tag, Tooltip } from "@blueprintjs/core"
import _ from "lodash"
import React from "react"
import { Col, Grid, Row } from "react-flexbox-grid"
import Moment from "react-moment"
import { useHistory } from "react-router-dom"
import styled from "styled-components"
import { RaidSubscription } from "../../api"
import { CharacterRole, characterRoleLabels, specToRoleMapping } from "../../api/utils"
import { useSession } from "../../hooks/useSession"
import { Routes } from "../../Routes"
import { SpecIcon } from "../spec-icons/SpecIcon"

const CharacterName = styled.span`
    margin-left: 0.2rem;
    color: ${(props: { user: boolean }) => (props.user ? "yellow" : "inherit")};
    &:hover {
        text-decoration: underline;
        cursor: pointer;
    }
`

const MainCharacterName = styled.span`
    color: gray;
    margin-left: 0.2rem;
    font-size: 80%;
`

const CharactersWrapper = styled.div`
    margin-bottom: 2rem;
`

const StyledTag = styled(Tag)`
    margin: 0 0.5rem;
`

export type SubscriptionModel = RaidSubscription & {
    mainCharacterName?: string
    index: number
}

const RaidSubscriptionOrder: React.FC<{ index: number; date: number }> = props => (
    <Tooltip content={<Moment format="DD/MM/YYYY HH:mm">{props.date}</Moment>}>
        <StyledTag minimal round>
            {props.index + 1}
        </StyledTag>
    </Tooltip>
)

const RaidSubscriptionList: React.FC<{ title: string; subscriptions: SubscriptionModel[]; showIndexes: boolean }> = props => {
    const session = useSession()
    const history = useHistory()

    const sortedSubscriptions = _.sortBy(
        props.subscriptions,
        s => s.character?.spec,
        s => s.character?.name,
        s => s.mainCharacterName,
        s => s.user.name
    )

    return (
        <>
            <H5>{`${props.title} (${sortedSubscriptions.length})`}</H5>
            <CharactersWrapper>
                {sortedSubscriptions.map(sub => (
                    <div key={sub.id}>
                        {sub.character && <SpecIcon spec={sub.character.spec} />}
                        {props.showIndexes && <RaidSubscriptionOrder index={sub.index} date={sub.date} />}
                        <CharacterName
                            user={session.user.id === sub.user.id}
                            onClick={() => history.push(Routes.user.show.create({ userId: String(sub.user.id) }))}
                        >
                            {sub.character?.name ?? sub.mainCharacterName ?? sub.user.name}
                        </CharacterName>
                        {sub.character && !sub.character.main && sub.mainCharacterName && (
                            <MainCharacterName>{sub.mainCharacterName}</MainCharacterName>
                        )}
                    </div>
                ))}
            </CharactersWrapper>
        </>
    )
}

export const SubscriptionList: React.FC<{ subscriptions: SubscriptionModel[]; showIndexes: boolean }> = props => {
    const presents = props.subscriptions.filter(sub => sub.response === "PRESENT")
    const absents = props.subscriptions.filter(sub => sub.response === "ABSENT")
    const lates = props.subscriptions.filter(sub => sub.response === "LATE")
    const benches = props.subscriptions.filter(sub => sub.response === "BENCH")

    const roleList = (role: CharacterRole) => {
        const subs = presents.filter(sub => sub.character && specToRoleMapping[sub.character.spec] === role)
        const title = characterRoleLabels[role]
        return <RaidSubscriptionList title={title} subscriptions={subs} showIndexes={props.showIndexes} />
    }

    return (
        <>
            <H4>Inscrits ({presents.length})</H4>
            <Grid>
                <Row>
                    <Col xs>{roleList(CharacterRole.TANK)}</Col>
                    <Col xs>{roleList(CharacterRole.MELEE)}</Col>
                    <Col xs>{roleList(CharacterRole.RANGED)}</Col>
                    <Col xs>{roleList(CharacterRole.HEAL)}</Col>
                </Row>
                <Row>
                    <Col xs>
                        <RaidSubscriptionList title="En retard" subscriptions={lates} showIndexes={props.showIndexes} />
                    </Col>
                    <Col xs>
                        <RaidSubscriptionList title="Bench" subscriptions={benches} showIndexes={props.showIndexes} />
                    </Col>
                    <Col xs>
                        <RaidSubscriptionList title="Absents" subscriptions={absents} showIndexes={props.showIndexes} />
                    </Col>
                    <Col xs />
                </Row>
            </Grid>
        </>
    )
}
