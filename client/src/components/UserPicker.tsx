import { Button, MenuItem } from "@blueprintjs/core"
import { Select } from "@blueprintjs/select"
import _ from "lodash"
import React, { useEffect, useState } from "react"
import { User } from "../api"
import { client } from "../api/client"

const UserSelect = Select.ofType<User>()
const normalizeForSearch = (str: string) => _.deburr(str).toLowerCase()

export const UserPicker: React.FC<{
    userId: number
    onChange: (id: number) => void
}> = props => {
    const [users, setUsers] = useState<User[]>()
    const selectedUser = users?.find(u => u.id === props.userId)

    useEffect(() => {
        client.users.findAllUsers().then(resp => setUsers(_.sortBy(resp.data, u => u.name)))
    }, [])

    return (
        <>
            {users && (
                <UserSelect
                    activeItem={selectedUser}
                    items={users}
                    onItemSelect={u => props.onChange(u.id)}
                    itemPredicate={(query, user) => normalizeForSearch(user.name).includes(normalizeForSearch(query))}
                    itemRenderer={(u, { modifiers }) => (
                        <MenuItem active={modifiers.active} key={u.id} text={u.name} onClick={() => props.onChange(u.id)} />
                    )}
                >
                    <Button text={selectedUser?.name} rightIcon="caret-down" />
                </UserSelect>
            )}
        </>
    )
}
