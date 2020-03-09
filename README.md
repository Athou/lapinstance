# Lapinstance

WoW Classic raid management and discord integration for Les Lapins de Lumière (EU-Sulfuron).

- Users log in with their discord account
- Guild master manages raids from the website
- Guild members can subscribe to raids from the website or via discord reactions
- Guild master can send reminders to members that are not yet subscribed to a raid
- Displays Onyxia resets on the calendar

![Raid calendar](https://i.imgur.com/gncwg4P.png "Raid calendar")

![Raid page](https://i.imgur.com/eVbqA6J.png "Raid page")

![Discord integration](https://i.imgur.com/b6InkwS.png "Discord integration")

## Build for production

```bash
mvnw clean install
```

## Develop on your machine

### Backend

Create an application on https://discordapp.com/developers/applications, copy (not rename)
`src/main/resources/application-default.yml.example` to `src/main/resources/application-default.yml`
and fill all the fields (icons are optional).

#### Start in an IDE

```
run LapinstanceApplication.java
```

#### Start from the command line

```bash
mvnw spring-boot:run
```

### Frontend

```bash
npm start
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](https://choosealicense.com/licenses/mit/)
