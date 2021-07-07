import React from "react"
import { Grid } from "react-flexbox-grid"
import { HashRouter as Router, Redirect, Route, Switch } from "react-router-dom"
import styled from "styled-components"
import "./App.css"
import { Header } from "./components/Header"
import { useSession } from "./hooks/useSession"
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

const App: React.FC = () => {
    const session = useSession()

    return (
        <Router>
            <Grid>
                <Header />
                <Content>
                    <Switch>
                        <Route path={Routes.raid.list.template()} component={RaidsPage} />
                        <Route path={Routes.raid.new.template()} component={RaidNewPage} />
                        <Route path={Routes.raid.show.template()} render={props => <RaidPage raidId={+props.match.params.raidId} />} />
                        <Route path={Routes.raid.edit.template()} render={props => <RaidEditPage raidId={+props.match.params.raidId} />} />

                        <Route path={Routes.user.list.template()} component={UsersPage} />
                        <Route path={Routes.user.show.template()} render={props => <UserPage userId={+props.match.params.userId} />} />

                        <Route path={Routes.profile.template()} render={() => <UserPage userId={session.user.id} />} />

                        <Redirect to={Routes.raid.list.create({})} />
                    </Switch>
                </Content>
            </Grid>
        </Router>
    )
}

export default App
