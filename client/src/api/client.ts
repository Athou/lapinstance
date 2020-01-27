import { Intent, Toaster } from "@blueprintjs/core"
import axios from "axios"
import {
    AxiosApplicationSettingsControllerClient,
    AxiosRaidControllerClient,
    AxiosRaidTypeControllerClient,
    AxiosRosterControllerClient,
    AxiosSessionControllerClient,
    AxiosUserCharacterControllerClient,
    AxiosUserControllerClient
} from "."

const toaster = Toaster.create()

const axiosInstance = axios.create()
axiosInstance.interceptors.response.use(
    response => response,
    error => {
        toaster.show({
            message: "Erreur: " + error,
            icon: "error",
            intent: Intent.DANGER
        })
        return Promise.reject(error)
    }
)

export const client = {
    raids: new AxiosRaidControllerClient("/", axiosInstance),
    raidTypes: new AxiosRaidTypeControllerClient("/", axiosInstance),
    rosters: new AxiosRosterControllerClient("/", axiosInstance),
    sessions: new AxiosSessionControllerClient("/", axiosInstance),
    applicationSettings: new AxiosApplicationSettingsControllerClient("/", axiosInstance),
    users: new AxiosUserControllerClient("/", axiosInstance),
    userCharacters: new AxiosUserCharacterControllerClient("/", axiosInstance)
}
