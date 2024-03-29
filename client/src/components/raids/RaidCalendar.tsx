import { Button, ButtonGroup } from "@blueprintjs/core"
import moment from "moment"
import React from "react"
import { Calendar, Event, EventPropGetter, momentLocalizer, ToolbarProps } from "react-big-calendar"
import { Box, Flex } from "reflexbox"
import styled from "styled-components"
import { Raid, RaidResetDuration } from "../../api"
import { raidTypeShortLabels } from "../../api/utils"
import "./RaidCalendar.css"

const localizer = momentLocalizer(moment)

const ToolbarLabelBox = styled(Box)`
    align-self: flex-end;
    margin-bottom: 0.5rem;
`

const ToolbarButtonGroup = styled(ButtonGroup)`
    margin-bottom: 0.5rem;
`

export type RaidReset = {
    date: number
    label: string
    raidResetDuration: RaidResetDuration
}

type EventPayload = { type: "raid"; raid: Raid } | { type: "reset"; reset: RaidReset }

const Toolbar: React.FC<ToolbarProps> = props => (
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

export const RaidCalendar: React.FC<{
    raids: Raid[]
    resets: RaidReset[]
    onRaidSelect: (raidId: number) => void
}> = props => {
    const events: Event[] = props.raids.map(raid => {
        const endDate = new Date(raid.date)
        endDate.setHours(23, 59, 59, 999)
        const resource: EventPayload = {
            type: "raid",
            raid,
        }
        return {
            title: `${moment(raid.date).format("HH:mm")} ${raidTypeShortLabels[raid.raidType]}${raid.raidSize ?? ""}`,
            start: new Date(raid.date),
            end: endDate,
            resource,
        }
    })

    props.resets.forEach(reset => {
        const resource: EventPayload = {
            type: "reset",
            reset,
        }
        events.push({
            title: `${moment(reset.date).format("HH:mm")} Reset ${reset.label}`,
            start: new Date(reset.date),
            end: new Date(reset.date),
            resource,
        })
    })

    const eventPropGetter: EventPropGetter<any> = event => {
        const resource = event.resource as EventPayload
        if (resource.type === "reset") {
            const color = resource.reset.raidResetDuration === RaidResetDuration.FIVE_DAYS ? "#793122" : "#4CA66B"
            return {
                style: {
                    backgroundColor: color,
                },
            }
        }

        return {}
    }

    return (
        <>
            <Calendar
                localizer={localizer}
                events={events}
                views={["month"]}
                eventPropGetter={eventPropGetter}
                onSelectEvent={(e: Event) => {
                    const resource = e.resource as EventPayload
                    if (resource.type === "raid") props.onRaidSelect(resource.raid.id)
                }}
                components={{ toolbar: Toolbar }}
                popup
            />
        </>
    )
}
