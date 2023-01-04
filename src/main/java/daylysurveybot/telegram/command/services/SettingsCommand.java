package daylysurveybot.telegram.command.services;

import daylysurveybot.Utils;
import daylysurveybot.telegram.Bot;
import daylysurveybot.telegram.noncommand.Settings;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class SettingsCommand extends ServiceCommand {

    public SettingsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        Long chatId = chat.getId();
        Settings settings = Bot.getUserSettings(chatId);
        sendAnswer(absSender, chatId, this.getCommandIdentifier(), userName,
                String.format("""
                                *Текущие настройки*
                                - ответ на приветствие бота - *%s*

                                Если Вы хотите изменить этот параметр, введите новое сообщение от бота

                                \uD83D\uDC49 Например, Привет!\s""",
                        settings.getHelloWorldAnswer()));
    }
}