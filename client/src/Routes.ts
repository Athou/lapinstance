import { param, route } from "typesafe-react-router"

export const Routes = {
    raid: {
        list: route("raids"),
        new: route("raid", "new"),
        show: route("raid", "show", param("raidId")),
        edit: route("raid", "edit", param("raidId"))
    },
    roster: route("roster"),
    profile: route("profile")
}
