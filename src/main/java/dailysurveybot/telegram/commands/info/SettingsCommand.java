package dailysurveybot.telegram.commands.info;

import dailysurveybot.telegram.Utils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.telegram.constants.CommandsEnum.SETTINGS;

@Component
public class SettingsCommand extends InfoCommand {

    public SettingsCommand() {
        super(SETTINGS.getIdentifier(), SETTINGS.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        Long chatId = chat.getId();
        sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName, "Настраивать пока нечего");
    }
}
