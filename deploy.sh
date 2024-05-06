gradle clean
gradle build spotlessApply
cp ./app/build/libs/app.jar ./app/build/libs/memeBotTelegramBot.jar
ssh doomayka@v01d.ru "cd telegram-bot && make kill"
scp ./app/build/libs/memeBotTelegramBot.jar doomayka@v01d.ru:~/telegram-bot
scp runner.py doomayka@v01d.ru:~/telegram-bot
scp runnerconfig.toml doomayka@v01d.ru:~/telegram-bot
ssh doomayka@v01d.ru "cd telegram-bot && make start"
