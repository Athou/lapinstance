import { Card, Elevation, H5, H6 } from "@blueprintjs/core"
import React from "react"
import Moment from "react-moment"
import styled from "styled-components"
import { Raid, RaidSubscription } from "../../api"
import { raidTypeLabels } from "../../api/utils"
import { Box, Flex } from "../flexbox"
import { ResponseIcon } from "../response-icons/ResponseIcon"
import { SpecIcon } from "../spec-icons/SpecIcon"

const StyledCard = styled(Card)`
    margin-bottom: 1em;
`

export const RaidCard: React.FC<{
    raid: Raid
    userSubscription?: RaidSubscription
    onClick: (raidId: number) => void
}> = props => {
    let icon
    if (props.userSubscription) {
        if (props.userSubscription.character) {
            icon = <SpecIcon spec={props.userSubscription.character.spec} />
        } else {
            icon = <ResponseIcon response={props.userSubscription.response} />
        }
    }

    return (
        <>
            <StyledCard elevation={Elevation.TWO} interactive onClick={() => props.onClick(props.raid.id!)}>
                <Flex>
                    <Box grow={1}>
                        <H5>{raidTypeLabels[props.raid.raidType]}</H5>
                    </Box>
                    <Box>{icon}</Box>
                </Flex>
                <H6 muted>
                    <Moment format="dddd DD MMMM YYYY HH:mm">{props.raid.date}</Moment>
                </H6>
                <p>{props.raid.comment}</p>
            </StyledCard>
        </>
    )
}
