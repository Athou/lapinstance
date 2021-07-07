import "@blueprintjs/core/lib/css/blueprint.css"
import "@blueprintjs/datetime/lib/css/blueprint-datetime.css"
import "@blueprintjs/icons/lib/css/blueprint-icons.css"
import moment from "moment"
import "moment/locale/fr"
import React, { Suspense } from "react"
import "react-big-calendar/lib/css/react-big-calendar.css"
import ReactDOM from "react-dom"
import App from "./App"
import { Loader } from "./components/Loader"
import "./react-big-calendar-overrides.css"

moment.locale("fr")
ReactDOM.render(
    <Suspense fallback={<Loader />}>
        <App />
    </Suspense>,
    document.getElementById("root")
)
