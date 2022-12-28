# Stundenplan app

School training purposes

## Description

This is a open private project for training purposes.

This project teaches me:
- Kotlin language
- Android Jetpack Compose
  - Components
  - State management
  - State/Data flow
- Kotlin HTTP-Requests
- Kotlin API-Wrapping

## Usage

### API-Wrapper

#### Creating an instance of the wrapper
---
```Kotlin
val userData = UserData(username = "m.mustermann", password = "password")
val api = ApiWrapper(userData)
```
or
```Kotlin
val userData = UserData(base64 = "encryptedCredentials")
val api = ApiWrapper(userData)
```

#### Logging in
```Kotlin
api.login()
```

#### Getting all events on a date
```Kotlin
// Type of List<stundenplan.model.EventData.Event>
val listOfEvents = api.getEvents("2022-12-31")
```

## Help

### Cloning this project

git shell:

```shell
git clone https://github.com/leonsuv/stundenplan.git
```

## Having an issue?

For any issue with this project, feel free to open an issue on the projects repository.

## Legal

All information over this API is publicly available at [https://app.phwt.de/](https://app.phwt.de/).

The developer is open about being informed for any legal conflicts created by this open project.

## Licence

This Project is licensed under the Apache 2.0 Licence.

For further information visit [leonsuv/stundenplan/LICENSE](https://github.com/leonsuv/stundenplan/blob/main/LICENSE)
