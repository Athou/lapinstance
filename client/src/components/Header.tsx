import { Button, Navbar, NavbarDivider, NavbarGroup, NavbarHeading } from "@blueprintjs/core"
import React from "react"
import { useHistory, useRouteMatch } from "react-router-dom"
import { useSession } from "../App"
import { Routes } from "../Routes"

export const Header: React.FC = () => {
    const history = useHistory()
    const session = useSession()

    const raidLinkActive = !!useRouteMatch(Routes.raid.list.template())
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
