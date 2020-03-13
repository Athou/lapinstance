import React, { useContext, useEffect, useState } from "react"
import { Grid } from "react-flexbox-grid"
import { HashRouter as Router, Redirect, Route, Switch } from "react-router-dom"
import styled from "styled-components"
import { User, UserRole } from "./api"
import { client } from "./api/client"
import "./App.css"
import { Header } from "./components/Header"
import { Loader } from "./components/Loader"
import { RaidEditPage } from "./pages/RaidEditPage"
import { RaidNewPage } from "./pages/RaidNewPage"
import { RaidPage } from "./pages/RaidPage"
import { RaidsPage } from "./pages/RaidsPage"
import { UserPage } from "./pages/UserPage"
import { UsersPage } from "./pages/UsersPage"
import { Routes } from "./Routes"

const Content = styled.div`
    margin-top: 1rem;
`

type Session = {
    user: User
    roles: UserRole[]
    hasRole: (role: UserRole) => boolean
}
const SessionContext = React.createContext<Session>({} as Session)
export const useSession = () => useContext(SessionContext)

const App: React.FC = () => {
    const [session, setSession] = useState<Session>()

    useEffect(() => {
        client.sessions.getCurrentUser().then(resp =>
            setSession({
                user: resp.data.user,
                roles: resp.data.roles,
                hasRole: role => resp.data.roles.some(r => r === role)
            })
        )
    }, [])

    if (!session) return <Loader />
    return (
        <>
            <SessionContext.Provider value={session}>
                <Router>
                    <Grid>
                        <Header />
                        <Content>
                            <Switch>
                                <Route path={Routes.raid.list.template()} component={RaidsPage} />
                                <Route path={Routes.raid.new.template()} component={RaidNewPage} />
                                <Route
                                    path={Routes.raid.show.template()}
                                    render={props => <RaidPage raidId={+props.match.params.raidId} />}
                                />
                                <Route
                                    path={Routes.raid.edit.template()}
                                    render={props => <RaidEditPage raidId={+props.match.params.raidId} />}
                                />

                                <Route path={Routes.user.list.template()} component={UsersPage} />
                                <Route
                                    path={Routes.user.show.template()}
                                    render={props => <UserPage userId={+props.match.params.userId} />}
                                />

                                <Route path={Routes.profile.template()} render={() => <UserPage userId={session.user.id} />} />

                                <Redirect to={Routes.raid.list.create({})} />
                            </Switch>
                        </Content>
                    </Grid>
                </Router>
            </SessionContext.Provider>
        </>
    )
}

export default App
