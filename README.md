# MemeBot

## Возможности

Данное приложение имеет следующие возможности:

- Взаимодействие с telegram чатом посредством бота.
- Поддержка конфигурации.
- Поддержка текстовых комманд.
- Поддержка текстовой и графической (изображения) рассылки.
- Авторассылка мемов.
- Настройка расписания авторассылки мемов.
- Возможность использования графического меню.

## Как запустить?

Последнюю версию приложения можно скачать с GitHub Releases.

GitHub Releases: [link](https://github.com/Doomaykaka/MemeBot/releases)

Для использования telegram бота его необходимо запустить.

```bash
java -jar app.jar
```

### Зависимости

- JDK 21 и выше
- Gradle 8.0.1 и выше

### Использование

Перед запуском telegram бот необходимо настроить. Для этого необходимо в той же директории где лежит исполняемый файл приложения создать файл settings.conf (имя файла должно строго соблюдаться). Ниже приведён пример такого конфигурационного файла.

```bash
chat-id=54874574578
bot-username=MyAmazingBot
token=384383934:AAFDkffjfjkJGfsdPoC_JxfdghdFFFlkokdoajkf
bot-backend-url=https://server.api.site/
backend-login=bot_login
backend-password=bot_password
```

Конфигурационный файл может содержать ряд параметров:

- Параметр chat-id отвечает за id telegram чата, где будет использоваться бот (принимает число).
- Параметр bot-username задаёт отображение имени telegram бота в системе (не влияет на никнейм бота).
- Параметр token задаёт токен по которому приложение подключт соответствующего telegram бота.
- Параметр bot-backend-url задаёт URL адрес сервера, который будет предоставлять telegram боту контент по соответствующему API.
- Параметр backend-login отвечает за логин для telegram бота на сервере.
- Параметр backend-password отвечает за пароль для telegram бота на сервере.

## Документация

Documentation: [link](https://doomaykaka.github.io/MemeBot/)
