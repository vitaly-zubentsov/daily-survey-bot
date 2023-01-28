package dailysurveybot.telegram.services;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

/**
 * Интерфейс обработки callback запроса
 */
public interface CallbackQueryService {

    BotApiMethod<? extends Serializable> processCallbackQuery(CallbackQuery buttonQuery);
}
