package dailysurveybot.telegram.commands.services;

import dailysurveybot.Utils;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.Settings;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.telegram.constants.CommandsEnum.SETTINGS;

@Component
public class SettingsCommand extends ServiceCommand {

    public SettingsCommand() {
        super(SETTINGS.getIdentifier(), SETTINGS.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        Long chatId = chat.getId();
        Settings settings = DailySurveyBot.getUserSettings(chatId);
        sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName,
                String.format("""
                                *Текущие настройки*
                                - ответ на приветствие бота - *%s*

                                Если Вы хотите изменить этот параметр, введите новое сообщение от бота

                                \uD83D\uDC49 Например, Привет!\s""",
                        settings.getHelloWorldAnswer()));
    }
}
