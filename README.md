# Lapinstance

WoW Classic raid management and discord integration for Les Lapins de Lumière

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
