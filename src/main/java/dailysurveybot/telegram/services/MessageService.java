package dailysurveybot.telegram.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

/**
 * Интерфейс обработки сообщений от пользователя
 */
public interface MessageService {

    /**
     * Обработка сообщений от пользователя
     *
     * @param message сообщение от пользователя
     * @return ответ пользователю
     */
    BotApiMethod<? extends Serializable> answerMessage(Message message);
}
