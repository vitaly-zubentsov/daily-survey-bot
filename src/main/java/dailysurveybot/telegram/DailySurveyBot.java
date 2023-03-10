package dailysurveybot.telegram;

import dailysurveybot.config.TelegramConfig;
import dailysurveybot.telegram.commands.info.HelpCommand;
import dailysurveybot.telegram.commands.info.SettingsCommand;
import dailysurveybot.telegram.commands.info.StartCommand;
import dailysurveybot.telegram.commands.operation.AddRowToTableCommand;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.noncommands.NonCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс телеграмм бота
 */
@Component
public class DailySurveyBot extends TelegramLongPollingCommandBot {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Данные пользователей. Ключ - уникальный id чата
    private static final Map<Long, UserData> usersData = new HashMap<>();

    private final String botName;
    private final String botToken;
    private final NonCommand nonCommand;

    public DailySurveyBot(TelegramConfig telegramConfig,
                          StartCommand startCommand,
                          SettingsCommand settingsCommand,
                          HelpCommand helpCommand,
                          NonCommand nonCommand,
                          AddRowToTableCommand addRowToTableCommand) {
        super();
        this.botName = telegramConfig.botName();
        this.botToken = telegramConfig.botToken();
        this.nonCommand = nonCommand;

        //регистрируем команды
        register(startCommand);
        logger.debug("Команда start была зарегистрирована");
        register(settingsCommand);
        logger.debug("Команда settings была зарегистрирована");
        register(helpCommand);
        logger.debug("Команда help была зарегистрирована");
        register(addRowToTableCommand);
        logger.debug("Команда AddRowToTableCommand(/t) была зарегистрирована");
        logger.debug("Бот был создан");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    private static Map<Long, UserData> getUsersData() {
        return usersData;
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        Long chatId;
        String userName;
        String textFromUser;
        if (update.hasCallbackQuery()) {
            //Получение данных при нажатии inline клавиатуры пользователем
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userName = Utils.getUserName(update.getCallbackQuery().getFrom());
            textFromUser = update.getCallbackQuery().getData();
        } else {
            //Получение данных из сообщения от пользователя
            chatId = update.getMessage().getChatId();
            userName = Utils.getUserName(update.getMessage().getFrom());
            textFromUser = update.getMessage().getText();
        }

        SendMessage answer = nonCommand.execute(chatId, userName, textFromUser);

        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error("Пользователь {}. Сбой при отправке сообщения в телеграмм. SendMessage {}", userName, answer);
        }
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены, используются настройки по умолчанию
     */
    public static UserData getUserData(Long chatId) {
        Map<Long, UserData> userDataMap = DailySurveyBot.getUsersData();
        UserData userData = userDataMap.get(chatId);
        if (userData == null) {
            UserData userDataDefault = new UserData(new ArrayList<>(), 0);
            userDataMap.put(chatId, userDataDefault);
            return userDataDefault;
        }
        return userData;
    }

}

