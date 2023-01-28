package dailysurveybot.telegram;

import dailysurveybot.telegram.services.CallbackQueryService;
import dailysurveybot.telegram.services.MessageService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.Serializable;

/**
 * Класс телеграмм бота
 */
public class DailySurveyBot extends SpringWebhookBot {

    private final String botPath;
    private final String botUsername;
    private final String botToken;

    private final MessageService messageService;
    private final CallbackQueryService callbackQueryHandler;


    public DailySurveyBot(SetWebhook setWebhook,
                          MessageService messageService,
                          CallbackQueryService callbackQueryHandler,
                          String botPath,
                          String botUsername,
                          String botToken) {
        super(setWebhook);
        this.messageService = messageService;
        this.callbackQueryHandler = callbackQueryHandler;
        this.botPath = botPath;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    /**
     * обработка запроса от пользователя
     *
     * @param update объект телеграмм api
     * @return ответ пользователю
     */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return process(update);
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "я работаю только с текстом");
        } catch (Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "я вообще не понимаю что проиходит");
        }
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private BotApiMethod<? extends Serializable> process(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageService.answerMessage(update.getMessage());
            }
        }
        return null;
    }
}
