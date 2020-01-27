import React from "react"
import styled from "styled-components"
import { RaidSubscriptionResponse } from "../../api"
import absent from "./absent.png"
import bench from "./bench.png"
import late from "./late.png"
import present from "./present.png"

const responseToIcon = {
    [RaidSubscriptionResponse.PRESENT]: present,
    [RaidSubscriptionResponse.LATE]: late,
    [RaidSubscriptionResponse.BENCH]: bench,
    [RaidSubscriptionResponse.ABSENT]: absent
}

const StyledImage = styled.img`
    vertical-align: middle;
`

export const ResponseIcon: React.FC<{ response: RaidSubscriptionResponse; size?: number }> = props => {
    return <StyledImage src={responseToIcon[props.response]} height={props.size ?? 16} />
}
