package dailysurveybot.telegram.commands;

import dailysurveybot.telegram.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.telegram.constants.CommandsEnum.HELP;

@Component
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super(HELP.getIdentifier(), HELP.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Вызываешь /t и заполняешь таблицу, чего непонятного то?");
    }
}
