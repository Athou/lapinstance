import styled from "styled-components"

// TODO find a lib to handle flexbox
export const Flex = styled.div`
    display: flex;
`

type BoxProps = {
    grow?: number
}

export const Box = styled.div`
    flex-grow: ${(props: BoxProps) => (props.grow ? props.grow : 0)};
`
