import { Button, ButtonGroup } from "@blueprintjs/core"
import moment from "moment"
import React from "react"
import { Calendar, Event, EventPropGetter, momentLocalizer, ToolbarProps } from "react-big-calendar"
import { Box, Flex } from "reflexbox"
import styled from "styled-components"
import { Raid, RaidType } from "../../api"
import { raidTypeShortLabels } from "../../api/utils"

const localizer = momentLocalizer(moment)

const StyedCalendar = styled(Calendar)`
    height: 600px;
`

const ToolbarLabelBox = styled(Box)`
    align-self: flex-end;
    margin-bottom: 0.5rem;
`

const ToolbarButtonGroup = styled(ButtonGroup)`
    margin-bottom: 0.5rem;
`

export type RaidReset = {
    date: number
    raidType: RaidType
}

export const RaidCalendar: React.FC<{
    raids: Raid[]
    resets: RaidReset[]
    onRaidSelect: (raidId: number) => void
}> = props => {
    const events: Event[] = props.raids.map(raid => {
        const endDate = new Date(raid.date)
        endDate.setHours(23, 59, 59, 999)
        return {
            title: `${moment(raid.date).format("HH:mm")} ${raidTypeShortLabels[raid.raidType]}`,
            start: new Date(raid.date),
            end: endDate,
            resource: raid.id
        }
    })

    props.resets.forEach(reset =>
        events.push({
            title: `${moment(reset.date).format("HH:mm")} Reset ${raidTypeShortLabels[reset.raidType]}`,
            start: new Date(reset.date),
            end: new Date(reset.date),
            resource: "reset"
        })
    )

    const eventPropGetter: EventPropGetter<any> = (event, start, end, isSelected) => {
        if (event.resource === "reset") {
            return {
                style: {
                    backgroundColor: "#793122"
                }
            }
        }

        return {}
    }

    return (
        <>
            <StyedCalendar
                localizer={localizer}
                events={events}
                views={["month"]}
                eventPropGetter={eventPropGetter}
                onSelectEvent={(e: Event) => e.resource !== "reset" && props.onRaidSelect(e.resource as number)}
                components={{ toolbar: Toolbar }}
            />
        </>
    )
}

const Toolbar: React.FC<ToolbarProps> = props => {
    return (
        <Flex>
            <ToolbarLabelBox flexGrow={1}>{props.label}</ToolbarLabelBox>
            <Box>
                <ToolbarButtonGroup>
                    <Button onClick={() => props.onNavigate("PREV")} icon="chevron-left" />
                    <Button onClick={() => props.onNavigate("TODAY")} icon="calendar" />
                    <Button onClick={() => props.onNavigate("NEXT")} icon="chevron-right" />
                </ToolbarButtonGroup>
            </Box>
        </Flex>
    )
}
