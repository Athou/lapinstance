import { Button, Navbar, NavbarDivider, NavbarGroup, NavbarHeading, Toaster, Tooltip } from "@blueprintjs/core"
import React from "react"
import { useHistory, useRouteMatch } from "react-router-dom"
import { UserRole } from "../api"
import { client } from "../api/client"
import { useSession } from "../hooks/useSession"
import { Routes } from "../Routes"

const toaster = Toaster.create()
export const Header: React.FC = () => {
    const history = useHistory()
    const session = useSession()

    const raidLinkActive = !!useRouteMatch(Routes.raid.list.template())
    const usersLinkActive = !!useRouteMatch(Routes.user.list.template())
    const profileLinkActive = !!useRouteMatch(Routes.profile.template())

    const refreshDiscordRolesClicked = () => {
        client.system.reconnect().then(() =>
            toaster.show({
                message: "Rôles rafraîchis",
                intent: "success",
                icon: "tick",
            })
        )
    }

    return (
        <Navbar>
            <NavbarGroup>
                <NavbarHeading>Lapinstance</NavbarHeading>
                <NavbarDivider />
                <Button
                    minimal
                    active={raidLinkActive}
                    icon="flame"
                    text="Raids"
                    onClick={() => history.push(Routes.raid.list.create({}))}
                />
                <Button
                    minimal
                    active={usersLinkActive}
                    icon="people"
                    text="Joueurs"
                    onClick={() => history.push(Routes.user.list.create({}))}
                />
            </NavbarGroup>
            <NavbarGroup align="right">
                {session.hasRole(UserRole.ADMIN) && (
                    <Tooltip content="Rafraîchir les rôles Discord">
                        <Button minimal icon="refresh" onClick={() => refreshDiscordRolesClicked()} />
                    </Tooltip>
                )}
                <Button
                    minimal
                    active={profileLinkActive}
                    icon="user"
                    text={session.user.name}
                    onClick={() => history.push(Routes.profile.create({}))}
                />
            </NavbarGroup>
        </Navbar>
    )
}
