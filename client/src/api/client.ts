import { Toaster } from "@blueprintjs/core"
import { Client } from "@stomp/stompjs"
import axios from "axios"
import {
    AxiosRaidControllerClient,
    AxiosRaidTextChannelControllerClient,
    AxiosRaidTypeControllerClient,
    AxiosSessionControllerClient,
    AxiosSystemControllerClient,
    AxiosUserCharacterControllerClient,
    AxiosUserControllerClient,
} from "."

const toaster = Toaster.create()

const axiosInstance = axios.create()
axiosInstance.interceptors.response.use(
    response => response,
    error => {
        toaster.show({
            message: `Erreur: ${error}`,
            icon: "error",
            intent: "danger",
        })
        return Promise.reject(error)
    }
)

const currentUrl = new URL(window.location.href)
const wsProtocol = currentUrl.protocol === "http:" ? "ws" : "wss"
// local dev proxy doesn't seem to work for websockets, bypass it
const wsPort = currentUrl.hostname === "localhost" ? "8760" : currentUrl.port
const wsUrl = `${wsProtocol}://${currentUrl.hostname}:${wsPort}/ws`

export const client = {
    raids: new AxiosRaidControllerClient("/", axiosInstance),
    raidTypes: new AxiosRaidTypeControllerClient("/", axiosInstance),
    raidTextChannels: new AxiosRaidTextChannelControllerClient("/", axiosInstance),
    sessions: new AxiosSessionControllerClient("/", axiosInstance),
    users: new AxiosUserControllerClient("/", axiosInstance),
    userCharacters: new AxiosUserCharacterControllerClient("/", axiosInstance),
    system: new AxiosSystemControllerClient("/", axiosInstance),
    newWsClient: () => new Client({ brokerURL: wsUrl }),
}
