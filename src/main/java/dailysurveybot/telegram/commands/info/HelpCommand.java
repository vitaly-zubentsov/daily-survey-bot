package dailysurveybot.telegram.commands.info;

import dailysurveybot.telegram.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.telegram.constants.CommandsEnum.HELP;

@Component
public class HelpCommand extends InfoCommand {

    public HelpCommand() {
        super(HELP.getIdentifier(), HELP.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Бот предназначен для заполнения таблиц в notion, содержащих колонки текста или выпадающих спискок(select). \nДля заполнения таблицы необходимо ввести настройки, для этого вызовите команду /start. \nПосле внесения данных можно заполнять таблицу введя команду /t.\n Если требуется изменить настройки повторно вызовите команду /start");
    }
}
