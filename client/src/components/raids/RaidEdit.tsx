import { FormGroup, HTMLSelect, TextArea } from "@blueprintjs/core"
import { DatePicker } from "@blueprintjs/datetime"
import moment from "moment"
import React, { useState } from "react"
import MomentLocaleUtils from "react-day-picker/moment"
import { useHistory } from "react-router-dom"
import { Raid, RaidTextChannel, RaidType, SaveRaidRequest } from "../../api"
import { client } from "../../api/client"
import { Expansion, expansionLabels, getExpansion, raidTypeExpansions, raidTypeLabels } from "../../api/utils"
import { Routes } from "../../Routes"
import { ActionButton } from "../ActionButton"

export const RaidEdit: React.FC<{ raid?: Raid; raidTextChannels: RaidTextChannel[] }> = props => {
    const newRaidDate = new Date()
    newRaidDate.setHours(20, 30, 0, 0)

    const datePickerMaxDate = moment().add(2, "years").toDate()

    const [expansion, setExpansion] = useState<Expansion>((props.raid && getExpansion(props.raid.raidType)) ?? "wotlk")
    const [raidType, setRaidType] = useState(props.raid?.raidType ?? RaidType.VAULT_OF_ARCHAVON)
    const [raidTextChannelId, setRaidTextChannelId] = useState(props.raid?.discordTextChannelId ?? props.raidTextChannels[0].id)
    const [date, setDate] = useState(props.raid?.date ?? newRaidDate.getTime())
    const [comment, setComment] = useState(props.raid?.comment)
    const [saving, setSaving] = useState(false)

    const history = useHistory()

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        const req: SaveRaidRequest = {
            raidId: props.raid?.id,
            raidType,
            raidTextChannelId,
            date,
            comment,
        }

        setSaving(true)
        client.raids
            .saveRaid(req)
            .then(() => history.push(Routes.raid.list.create({})))
            .finally(() => setSaving(false))
    }

    const handleCancel = () => history.push(Routes.raid.list.create({}))

    return (
        <>
            <form onSubmit={handleSubmit}>
                <FormGroup label="Extension">
                    <HTMLSelect value={expansion} onChange={e => setExpansion(e.target.value as Expansion)}>
                        {Object.entries(expansionLabels).map(([key, value]) => (
                            <option key={key} value={key}>
                                {value}
                            </option>
                        ))}
                    </HTMLSelect>
                </FormGroup>
                <FormGroup label="Raid">
                    <HTMLSelect value={raidType} onChange={e => setRaidType(e.target.value as RaidType)}>
                        {raidTypeExpansions[expansion].map(type => (
                            <option key={type} value={type}>
                                {raidTypeLabels[type]}
                            </option>
                        ))}
                    </HTMLSelect>
                </FormGroup>

                <FormGroup label="Channel">
                    <HTMLSelect value={raidTextChannelId} onChange={e => setRaidTextChannelId(e.target.value)}>
                        {props.raidTextChannels.map(chan => (
                            <option key={chan.id} value={chan.id}>
                                {chan.name}
                            </option>
                        ))}
                    </HTMLSelect>
                </FormGroup>

                <FormGroup label="Date">
                    <DatePicker
                        highlightCurrentDay
                        canClearSelection={false}
                        locale="fr"
                        localeUtils={MomentLocaleUtils}
                        value={new Date(date)}
                        onChange={d => setDate(d.getTime())}
                        timePrecision="minute"
                        maxDate={datePickerMaxDate}
                    />
                </FormGroup>

                <FormGroup label="Commentaire">
                    <TextArea fill value={comment} onChange={e => setComment(e.target.value)} />
                </FormGroup>

                <ActionButton intent="primary" type="submit" text="Enregistrer" loading={saving} marginRight />
                <ActionButton text="Annuler" onClick={handleCancel} />
            </form>
        </>
    )
}
