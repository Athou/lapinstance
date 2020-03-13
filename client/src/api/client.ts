import { Toaster } from "@blueprintjs/core"
import axios from "axios"
import {
    AxiosRaidControllerClient,
    AxiosRaidTypeControllerClient,
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
            intent: "danger"
        })
        return Promise.reject(error)
    }
)

export const client = {
    raids: new AxiosRaidControllerClient("/", axiosInstance),
    raidTypes: new AxiosRaidTypeControllerClient("/", axiosInstance),
    sessions: new AxiosSessionControllerClient("/", axiosInstance),
    users: new AxiosUserControllerClient("/", axiosInstance),
    userCharacters: new AxiosUserCharacterControllerClient("/", axiosInstance)
}
