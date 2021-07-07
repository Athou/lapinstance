import { atom, useAtom } from "jotai"
import { User, UserRole } from "../api"
import { client } from "../api/client"

type Session = {
    user: User
    roles: UserRole[]
    hasRole: (role: UserRole) => boolean
}

const sessionAtom = atom<Session>(async () => {
    const resp = await client.sessions.getCurrentUser()
    return {
        user: resp.data.user,
        roles: resp.data.roles,
        hasRole: role => resp.data.roles.some(r => r === role),
    }
})

export const useSession = () => useAtom(sessionAtom)[0]
