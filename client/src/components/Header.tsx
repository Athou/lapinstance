import { Button, Navbar, NavbarDivider, NavbarGroup, NavbarHeading } from "@blueprintjs/core"
import React from "react"
import { useHistory, useRouteMatch } from "react-router-dom"
import { UserRole } from "../api"
import { useApplicationSettings, useSession } from "../App"
import { Routes } from "../Routes"

export const Header: React.FC = () => {
    const history = useHistory()
    const session = useSession()
    const applicationSettings = useApplicationSettings()

    const raidLinkActive = !!useRouteMatch(Routes.raid.list.template())
    const rosterLinkActive = !!useRouteMatch(Routes.roster.template())
    const usersLinkActive = !!useRouteMatch(Routes.user.list.template())
    const profileLinkActive = !!useRouteMatch(Routes.profile.template())

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
                {applicationSettings.roasterEnabled && session.hasRole(UserRole.ADMIN) && (
                    <Button
                        minimal
                        active={rosterLinkActive}
                        icon="person"
                        text="Roster"
                        onClick={() => history.push(Routes.roster.create({}))}
                    />
                )}
            </NavbarGroup>
            <NavbarGroup align="right">
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
