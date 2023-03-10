package dailysurveybot.telegram.commands.info;


import dailysurveybot.telegram.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.telegram.constants.CommandsEnum.START;

@Component
public class StartCommand extends InfoCommand {

    public StartCommand() {
        super(START.getIdentifier(), START.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Давайте начнём! Я заполняю таблицу в notion. Достаточно набрать команду /t. Если Вам нужна помощь, нажмите /help");
    }
}
