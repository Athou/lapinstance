import { Button } from "@blueprintjs/core"
import styled from "styled-components"

type ActionButtonProps = {
    marginLeft?: boolean
    marginRight?: boolean
}
export const ActionButton = styled(Button)<ActionButtonProps>`
    margin-left: ${props => (props.marginLeft ? "0.5rem" : "0")};
    margin-right: ${props => (props.marginRight ? "0.5rem" : "0")};
`
