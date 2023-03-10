package dailysurveybot.telegram.commands.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

abstract class OperationCommand extends BotCommand {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    OperationCommand(String identifier, String description) {
        super(identifier, description);
    }

    /**
     * Отправка ответа пользователю
     */
    void sendAnswer(AbsSender absSender, String commandName, String userName, SendMessage answer) {
        //включаем поддержку режима разметки, чтобы управлять отображением текста и добавлять эмодзи
        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            logger.error("Пользователь {}. Не удалось обработать команду {}, ошибка {}",
                    userName,
                    commandName,
                    e.getMessage());
            e.printStackTrace();
        }
    }
}